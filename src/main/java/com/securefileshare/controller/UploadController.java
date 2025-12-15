package com.securefileshare.controller;

import com.securefileshare.model.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Controller
public class UploadController {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @PostMapping("/upload")
    public String handleFileUpload(
            @RequestParam("file") MultipartFile file,
            HttpSession session,
            RedirectAttributes redirectAttributes
    ) {

        /* ===============================
           1️⃣ AUTH CHECK (DO NOT CHANGE)
           =============================== */
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/login";
        }

        /* ===============================
           2️⃣ EMPTY FILE CHECK
           =============================== */
        if (file == null || file.isEmpty()) {
            redirectAttributes.addFlashAttribute(
                    "message",
                    "Please select a file to upload"
            );
            return "redirect:/dashboard";
        }

        try {
            /* ===============================
               3️⃣ ENSURE UPLOAD DIRECTORY
               =============================== */
            File uploadDirectory = new File(uploadDir);
            if (!uploadDirectory.exists()) {
                boolean created = uploadDirectory.mkdirs();
                if (!created) {
                    throw new IOException("Failed to create upload directory");
                }
            }

            /* ===============================
               4️⃣ SAVE FILE
               =============================== */
            String originalFilename = file.getOriginalFilename();
            File destination = new File(uploadDirectory, originalFilename);
            file.transferTo(destination);

            /* ===============================
               5️⃣ CREATE SHAREABLE LINK
               =============================== */
            String encodedFilename =
                    URLEncoder.encode(originalFilename, StandardCharsets.UTF_8);

            String fileUrl = "/files/" + encodedFilename;

            /* ===============================
               6️⃣ FLASH ATTRIBUTES FOR UI
               =============================== */
            redirectAttributes.addFlashAttribute(
                    "message",
                    "File uploaded successfully: " + originalFilename
            );

            redirectAttributes.addFlashAttribute(
                    "fileUrl",
                    fileUrl
            );

            return "redirect:/dashboard";

        } catch (Exception e) {

            e.printStackTrace();

            redirectAttributes.addFlashAttribute(
                    "message",
                    "File upload failed: " + e.getMessage()
            );

            return "redirect:/dashboard";
        }
    }
}
