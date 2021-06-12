package com.xzcube.community.controller;

import com.xzcube.community.dto.FileDTO;
import com.xzcube.community.utils.AliyunOSSUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author xzcube
 * @date 2021/6/7 10:40
 */
@Controller
public class UpLoadController {
    private static final String TO_PATH = "upload";
    private static final String RETURN_PATH = "success";

    @Autowired
    private AliyunOSSUtil aliyunOSSUtil;

    @RequestMapping("/toUpLoadFile")
    public String toUpLoadFile() {
        return TO_PATH;
    }

    /**
     * 文件上传
     */
    @RequestMapping(value = "/file/upload")
    @ResponseBody
    public FileDTO upload(HttpServletRequest request){
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile file = multipartRequest.getFile("editormd-image-file");
        try {
            String urlFileName = aliyunOSSUtil.upload(file.getInputStream(),file.getOriginalFilename());
            FileDTO fileDTO = new FileDTO();
            fileDTO.setSuccess(1);
            fileDTO.setUrl(urlFileName);
            return fileDTO;
        } catch (IOException e) {
            e.printStackTrace();
        }
        FileDTO fileDTO = new FileDTO();
        fileDTO.setSuccess(1);
        fileDTO.setUrl("/images/iceman.png");
        return fileDTO;
    }

}
