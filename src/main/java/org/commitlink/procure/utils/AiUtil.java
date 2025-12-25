package org.commitlink.procure.utils;

import java.util.Objects;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;

public class AiUtil {

  public static String aiQuery(String query, ChatClient client) {
    Objects.requireNonNull(query);
    String chatInput =
      """
                Here is the content from pdf file, I want to format this content into json. \
                Please provide me a json content of this: %s \n

              Task:
              Extract structured purchase and billing information from the provided text and return it only as valid JSON.

              Extraction Rules
              1. Item Extraction

              For each item mentioned:

              item_name: Identify and extract the product name only.

              quantity: Extract the numerical quantity (must be a number).

              unit_price: Extract the numerical unit price.

              total: Extract the numerical total price.

              Important:

              Ignore irrelevant words such as descriptions, page numbers (e.g., 200-page, ruled, pages), sizes, or any non-pricing details.

              Do not include extra text or explanations.

              2. Metadata Extraction

              created_by: Extract the name of the person who created the purchase (if present).

              purchase_details: Extract the purpose or description of the purchase.

              billing_info:

              Extract invoice number, date, status, and payment information if available.

              Output Format (STRICT)
              Return only a valid JSON object using the following structure:

              {
                "created_by": "string | null",
                "purchase_details": "string | null",
                "items": [
                  {
                    "item_name": "string",
                    "quantity": number,
                    "unit_price": number,
                    "total": number
                  }
                ],
                "billing_info": {
                  "invoice_no": "string | null",
                  "date": "string | null",
                  "status": "string | null",
                  "payment_info": {
                    "mobile_money": "string | null",
                    "due_date": "string | null"
                  }
                }
              }

              Constraints

              Return JSON only (no explanations, no markdown).

              Use null if a field is not present in the text.

              Ensure all numeric values are numbers, not strings.

              Do not infer or calculate missing values unless explicitly provided.
               """.formatted(
          query
        );
    Prompt prompt = new Prompt(chatInput);
    ChatClient.ChatClientRequestSpec requestSpec = client.prompt(prompt);
    ChatClient.CallResponseSpec responseSpec = requestSpec.call();
    return responseSpec.content();
  }
}
