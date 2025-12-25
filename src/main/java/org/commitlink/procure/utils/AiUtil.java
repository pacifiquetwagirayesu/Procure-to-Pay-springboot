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

              Output Format (STRICT)
              Return only a valid JSON object using the following structure:

              {
                "created_by": "string | null",
                "purchase_details": "string | null",
                "items": [
                  {
                    "item_name": "string",
                    "description": "string"
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
