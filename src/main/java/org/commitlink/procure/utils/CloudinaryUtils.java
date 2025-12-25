package org.commitlink.procure.utils;

import static org.commitlink.procure.utils.Constants.CLOUDINARY_UPLOAD_FOLDER;
import static org.commitlink.procure.utils.Constants.FOLDER;
import static org.commitlink.procure.utils.Constants.OVERWRITE;
import static org.commitlink.procure.utils.Constants.PUBLIC_ID;
import static org.commitlink.procure.utils.Constants.UNIQUE_FILENAME;
import static org.commitlink.procure.utils.Constants.USE_FILENAME;

import com.cloudinary.utils.ObjectUtils;
import java.util.Map;

public class CloudinaryUtils {

  public static Map uploadParams(String fileName) {
    return ObjectUtils.asMap(
      USE_FILENAME,
      true,
      UNIQUE_FILENAME,
      false,
      OVERWRITE,
      true,
      PUBLIC_ID,
      fileName,
      FOLDER,
      CLOUDINARY_UPLOAD_FOLDER
    );
  }
}
