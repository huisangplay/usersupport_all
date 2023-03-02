package com.comac.usersupport_tool.controller;

import com.comac.usersupport_tool.util.WordToPdfUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Random;

@Controller
@RequestMapping("/pdf")
public class PdfController {

    private static final Logger logger = LoggerFactory.getLogger(PdfController.class);
    private static final String PDFCONVERTTEMPFILEPATH = "F:\\convertPdfTestFolder";

    @GetMapping("/uploadFile")
    public String uploadFile() {
        return "pdf/pdfUpload";
    }

    @PostMapping("/uploadFile")
    @ResponseBody
    public String uploadFile(@RequestParam("file") MultipartFile uploadFile, HttpServletResponse response) {
        if (uploadFile.isEmpty()) {
            return "上传失败，请选择文件";
        }
        try {
            Random random = new Random();
            String docFileName = random.nextInt() + uploadFile.getOriginalFilename();

            String docFilePath = "F:\\convertPdfTestFolder\\" + docFileName;
            String pdfFilePath = "F:\\convertPdfTestFolder\\" + docFileName.substring(0, docFileName.lastIndexOf(".")) + ".pdf";

            uploadFile.transferTo(new File(docFilePath));

            WordToPdfUtils.wpsOfficeToPdf(docFilePath, pdfFilePath);
            if(Files.exists(Path.of(docFilePath))) Files.delete(Path.of(docFilePath));
            if (Files.exists(Path.of(pdfFilePath))) {
                downloadFile(response, pdfFilePath, uploadFile.getOriginalFilename().substring(0, uploadFile.getOriginalFilename().lastIndexOf(".")) + ".pdf");
                if(Files.exists(Path.of(pdfFilePath))) Files.delete(Path.of(pdfFilePath));
                return "转换成功!";
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "转换失败";
    }


    // 路由注解可添加consumes参数指定Content-Type类型，如application/octet-stream
    @PostMapping(value = "/upload")
    public void convertDocToPdf(@RequestParam("file") MultipartFile multipartFile, HttpServletResponse response) {
        String docFileName = multipartFile.getOriginalFilename();
        String pdfFileName = docFileName.substring(0, docFileName.lastIndexOf(".")) + ".pdf";
        String docFilePath=PDFCONVERTTEMPFILEPATH + "\\" + docFileName;
        String pdfFilePath=PDFCONVERTTEMPFILEPATH + "\\" + pdfFileName;
        try {
            multipartFile.transferTo(new File("F:\\convertPdfTestFolder\\" + docFileName));

            logger.info("docFileName:" + docFileName);
            logger.info("pdfFileName:" + pdfFileName);

            WordToPdfUtils.wpsOfficeToPdf(docFilePath, pdfFilePath);
            if(Files.exists(Path.of(docFilePath))) Files.delete(Path.of(docFilePath));
            if (Files.exists(Path.of(pdfFilePath))) {
                getPdfStream(response, pdfFilePath, pdfFileName);
                if(Files.exists(Path.of(pdfFilePath))) Files.delete(Path.of(pdfFilePath));
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    public void getPdfStream(HttpServletResponse response, String pdfFilePath, String filename) throws Exception {
        FileInputStream pdfInputStream = new FileInputStream(pdfFilePath);
        byte[] data = new byte[pdfInputStream.available()];
        pdfInputStream.read(data);
        response.setContentType("application/octet-stream;charset=UTF-8");
        response.setHeader("Content-Disposition", "form-data;name=\"file\"; filename=\"" + new String(filename.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1) + "\"");
        response.setContentLength(data.length);
        response.setHeader("Content-Range", "" + (data.length - 1));
        response.setHeader("Accept-Ranges", "bytes");
        OutputStream os = response.getOutputStream();

        os.write(data);
        logger.info("length:" + data.length);
        //先声明的流后关掉！
        os.flush();
        os.close();
        pdfInputStream.close();
    }

    public void downloadFile(HttpServletResponse response, String pdfFilePath, String filename) throws Exception {
        FileInputStream pdfInputStream = new FileInputStream(pdfFilePath);
        byte[] data = new byte[pdfInputStream.available()];
        pdfInputStream.read(data);
        response.setContentType("application/x-download");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + new String(filename.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1) + "\"");
        System.out.println("data.length " + data.length);
        response.setContentLength(data.length);
        response.setHeader("Content-Range", "" + (data.length - 1));
        response.setHeader("Accept-Ranges", "bytes");
        OutputStream os = response.getOutputStream();

        os.write(data);
        //先声明的流后关掉！
        os.flush();
        os.close();
        pdfInputStream.close();
    }

}
