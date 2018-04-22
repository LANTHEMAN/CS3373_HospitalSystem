package edu.wpi.cs3733d18.teamF.face;

import java.io.File;
import java.net.URI;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;

import com.github.sarxos.webcam.Webcam;
import edu.wpi.cs3733d18.teamF.controller.PermissionSingleton;
import edu.wpi.cs3733d18.teamF.db.DatabaseSingleton;
import javafx.scene.image.Image;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.imageio.ImageIO;

public class FaceLauncher {
    public static final String subscriptionKey = "a5c0adfe4de64971af317d784209a2d2";
    public static final String uriBase = "https://westcentralus.api.cognitive.microsoft.com/face/v1.0/detect";
    public static final String compareBase = "https://westcentralus.api.cognitive.microsoft.com/face/v1.0/verify";
    HttpClient httpclient = new DefaultHttpClient();
    Webcam webcam;

    public FaceLauncher() {
        try {
            webcam = Webcam.getDefault(5000);
        }catch(Exception e) {
            System.out.println("Webcam timeout, no webcam found");
        }
    }

    public String getEmployeeName(String faceID) {
        try {
            URIBuilder builder = new URIBuilder(compareBase);

            // Request parameters. All of them are optional.
            builder.setParameter("returnFaceId", "true");
            builder.setParameter("returnFaceLandmarks", "false");

            // Prepare the URI for the REST API call.
            URI uri = builder.build();
            HttpPost request = new HttpPost(uri);

            // Request headers.
            request.setHeader("Content-Type", "application/json");
            request.setHeader("Ocp-Apim-Subscription-Key", subscriptionKey);

            HashMap<String, String> unameFace = PermissionSingleton.getInstance().getUserAndFace();

            for (String key : unameFace.keySet()) {

                String val = unameFace.get(key);

                StringEntity reqEntity = new StringEntity("{\"faceId1\": \"" + val + "\"," +
                        "\"faceId2\": \"" + faceID + "\"}");
                request.setEntity(reqEntity);

                // Execute the REST API call and get the response entity.
                HttpResponse response = httpclient.execute(request);
                HttpEntity entity = response.getEntity();

                if (entity != null) {
                    String jsonString = EntityUtils.toString(entity).trim();

                    if (jsonString.substring(15, 19).equals("true")) {
                        return key;
                    }
                }
            }
            return "false";

        } catch (Exception e) {
            // Display error message.
            System.out.println(e.getMessage());
            return "false";
        }
    }

    public String getCameraFaceID() {
        try {
            URIBuilder builder = new URIBuilder(uriBase);

            // Request parameters. All of them are optional.
            builder.setParameter("returnFaceId", "true");
            builder.setParameter("returnFaceLandmarks", "false");

            // Prepare the URI for the REST API call.
            URI uri = builder.build();
            HttpPost request = new HttpPost(uri);

            // Request headers.
            request.setHeader("Content-Type", "application/octet-stream");
            request.setHeader("Ocp-Apim-Subscription-Key", subscriptionKey);

            File imageFile = new File("curFace.png");

            if (!imageFile.exists()) {
                webcam.open();
                ImageIO.write(webcam.getImage(), "PNG", imageFile);
                webcam.close();
            }

            FileEntity reqEntity = new FileEntity(imageFile, ContentType.APPLICATION_OCTET_STREAM);
            request.setEntity(reqEntity);

            // Execute the REST API call and get the response entity.
            HttpResponse response = httpclient.execute(request);
            HttpEntity entity = response.getEntity();

            imageFile.delete();

            if (entity != null) {
                String jsonString = EntityUtils.toString(entity).trim();
                String newFaceID = jsonString.substring(12, 48);

                return newFaceID;
            } else {
                return "false";
            }
        } catch (Exception e) {
            // Display error message.
            System.out.println(e.getMessage());
            return "false";
        }
    }
}
