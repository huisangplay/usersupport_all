package com.comac.usersupport_tool.util;

import java.io.File;
import java.nio.file.Files;
import java.util.Objects;

import com.comac.usersupport_tool.controller.PdfController;
import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComThread;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Word转PDF工具类
 */
public class WordToPdfUtils {

    private static final Logger logger = LoggerFactory.getLogger(PdfController.class);

    /**
     * word 转换为 pdf 的格式宏，值为 17
     */
    private static final int WORD_FORMAT_PDF = 17;
    private static final String MS_OFFICE_APPLICATION = "Word.Application";
    private static final String WPS_OFFICE_APPLICATION = "KWPS.Application";

    /**
     * 微软Office Word转PDF
     * 如果无法转换，可能需要下载 SaveAsPDFandXPS.exe 插件并安装
     *
     * @param wordFile Word文件
     * @param pdfFile  Pdf文件
     */
    public static void msOfficeToPdf(String wordFile, String pdfFile) {
        wordToPdf(wordFile, pdfFile, MS_OFFICE_APPLICATION);
    }

    /**
     * WPS Office Word转PDF
     *
     * @param wordFile Word文件
     * @param pdfFile  Pdf文件
     */
    public static void wpsOfficeToPdf(String wordFile, String pdfFile) {
        wordToPdf(wordFile, pdfFile, WPS_OFFICE_APPLICATION);
    }

    /**
     * Word 转 PDF
     *
     * @param wordFile    Word文件
     * @param pdfFile     Pdf文件
     * @param application Office 应用
     */
    private static void wordToPdf(String wordFile, String pdfFile, String application) {
        Objects.requireNonNull(wordFile);
        Objects.requireNonNull(pdfFile);
        Objects.requireNonNull(application);

        ActiveXComponent app = null;
        Dispatch document = null;
        try {
            File outFile = new File(pdfFile);
            // 如果目标路径不存在, 则新建该路径，否则会报错
            if (!outFile.getParentFile().exists()) {
                Files.createDirectories(outFile.getParentFile().toPath());
            }

            // 如果目标文件存在，则先删除
            if (outFile.exists()) {
                outFile.delete();
            }

            // 这里需要根据当前环境安装的是 MicroSoft Office还是WPS来选择
            // 如果安装的是WPS，则需要使用 KWPS.Application
            // 如果安装的是微软的 Office，需要使用 Word.Application
            app = new ActiveXComponent(application);
            app.setProperty("Visible", new Variant(false));
            app.setProperty("AutomationSecurity", new Variant(3));

            Dispatch documents = app.getProperty("Documents").toDispatch();
            //document = Dispatch.call(documents, "Open", wordFile, false, true).toDispatch();
            document = Dispatch.call(documents, "Open", wordFile, false, true, false, new Variant("12")).toDispatch();
            Dispatch.call(document, "ExportAsFixedFormat", pdfFile, WORD_FORMAT_PDF);
        } catch (Exception e) {
            if (e.getMessage().contains("文档打开失败")) {
                logger.error("文档[" + wordFile + "]为加密文档，跳过转换");
            } else {
                e.printStackTrace();
            }

        } finally {
            if (document != null) {
                Dispatch.call(document, "Close", false);
            }

            if (app != null) {
                app.invoke("Quit", 0);
            }

            ComThread.Release();
        }
    }
}