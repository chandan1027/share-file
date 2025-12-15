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

@Controller
public class UploadController {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @PostMapping("/upload")
    public String handleFileUpload(
            @RequestParam("file") MultipartFile file,
            HttpSession session,
            RedirectAttributes redirectAttributes
    ) throws IOException {

        // ✅ CORRECT session check
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/login";
        }

        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Please select a file");
            return "redirect:/dashboard";
        }

        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File destination = new File(dir, file.getOriginalFilename());
        file.transferTo(destination);

        String filename = file.getOriginalFilename();
String fileUrl = "/files/" + filename;

redirectAttributes.addFlashAttribute(
        "message",
        "File uploaded successfully: " + filename
);
redirectAttributes.addFlashAttribute(
        "fileUrl",
        fileUrl
);


        return "redirect:/dashboard";
    }
}

