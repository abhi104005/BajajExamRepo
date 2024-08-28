package exam;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.Random;
import java.io.File;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class BajajExam {

	public static String findPath(String filePath) throws IOException {
        ObjectMapper obj = new ObjectMapper();
        JsonNode r = obj.readTree(new File(filePath));
        return Json(r);
    }
	
	
	private static String Json(JsonNode node) {
        if (node == null) return "";

        // If the current node is an object
        if (node.isObject()) {
            Iterator<String> fieldNames = node.fieldNames();
            while (fieldNames.hasNext()) {
                String fieldName = fieldNames.next();
                if (fieldName.equals("destination")) {
                    return node.get(fieldName).asText();
                }
                String result = Json(node.get(fieldName));
                if (!result.isEmpty()) {
                    return result;
                }
            }
        } else if (node.isArray()) {
            // If the current node is an array
            for (JsonNode arrayElement : node) {
                String result = Json(arrayElement);
                if (!result.isEmpty()) {
                    return result;
                }
            }
        }

        return "";
    }
	
	
	public static String generateRandomString(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder randomString = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            randomString.append(chars.charAt(random.nextInt(chars.length())));
        }
        return randomString.toString();
    }
	
	
	public static String generateHash(String input) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] hashBytes = md.digest(input.getBytes());
        StringBuilder sb = new StringBuilder();
        for (byte b : hashBytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
	
	public static void main(String[] args) {
		 if (args.length < 2) {
	            System.out.println("Usage: java -jar DestinationHashGenerator.jar <PRN_Number> <JSON_File_Path>");
	            return;
	        }
		 
		 String prnNumber = args[0].toLowerCase().trim();
	     String jsonFilePath = args[1];
	     
	     try {
	            String destinationValue = findPath(jsonFilePath);
	            String randomString = generateRandomString(8);

	            String concatenatedString = prnNumber + destinationValue + randomString;
	            String md5Hash = generateHash(concatenatedString);

	            System.out.println(md5Hash + ";" + randomString);

	        }catch (IOException e) {
	            System.err.println("Error reading or parsing the JSON file: " + e.getMessage());
	        } catch (NoSuchAlgorithmException e) {
	            System.err.println("Error generating MD5 hash: " + e.getMessage());
	        }
	     
	}

}
