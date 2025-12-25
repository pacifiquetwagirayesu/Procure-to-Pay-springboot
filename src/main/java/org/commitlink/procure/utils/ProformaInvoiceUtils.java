package org.commitlink.procure.utils;

import static org.commitlink.procure.utils.Constants.ERROR;
import static org.commitlink.procure.utils.PurchaseRequestMapper.calculateTotalAmount;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import lombok.extern.slf4j.Slf4j;
import org.commitlink.procure.dto.purchase.proforma.ProformaMetaDataItemDTO;
import org.commitlink.procure.dto.purchase.proforma.ProformaMetadataDTO;
import org.commitlink.procure.models.purchase.PurchaseItem;
import org.commitlink.procure.models.purchase.PurchaseRequest;
import org.commitlink.procure.repository.IPurchaseRequestRepository;
import org.springframework.ai.chat.client.ChatClient;

@Slf4j
public class ProformaInvoiceUtils {

  public static void extractProformaMetadataAndSave(
    PurchaseRequest purchaseRequest,
    IPurchaseRequestRepository repository,
    ExecutorService executorService,
    ChatClient chatClient,
    String invoiceData
  ) {
    Runnable task = () -> {
      Thread.currentThread().setName("AI THREAD");
      String aiResponse = AiUtil.aiQuery(invoiceData, chatClient);

      int start = aiResponse.indexOf('{');
      int end = aiResponse.lastIndexOf('}');

      if (start == -1 || end == -1 || start > end) {
        throw new IllegalArgumentException("No valid JSON found in AI response");
      }

      String json = aiResponse.substring(start, end + 1);

      ObjectMapper mapper = new ObjectMapper();
      try {
        JsonNode jsonNode = mapper.readTree(json);
        log.info("jsonNode: {}", jsonNode);
        purchaseRequest.setProformaMetadata(String.valueOf(jsonNode));
        if (purchaseRequest.getItems() == null) {
          ProformaMetadataDTO proformaMetadataDTO = mapper.readValue(String.valueOf(jsonNode), ProformaMetadataDTO.class);
          List<PurchaseItem> items = new ArrayList<>();
          for (ProformaMetaDataItemDTO item : proformaMetadataDTO.items()) {
              items
              .add(
                PurchaseItem
                  .builder()
                  .itemName(item.item_name())
                  .quantity(item.quantity())
                  .totalPrice(item.total())
                  .unitPrice(item.unit_price())
                  .build()
              );
          }

          purchaseRequest.setItems(items);
          BigDecimal totalAmount = calculateTotalAmount.apply(purchaseRequest.getItems());
          if (!Objects.equals(totalAmount, BigDecimal.ZERO)) purchaseRequest.setAmount(totalAmount);
          purchaseRequest.setAmount(totalAmount);

        }
        repository.save(purchaseRequest);
      } catch (JsonProcessingException e) {
        throw new RuntimeException(e);
      }
    };

    try {
      executorService.execute(task);
    } catch (Exception e) {
      log.info(ERROR, e.getMessage());
    } finally {
      log.info("TASK EXECUTED: {}", LocalTime.now());
    }
  }
}
