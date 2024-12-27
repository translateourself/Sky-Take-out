package com.sky.controller.admin;

import com.sky.constant.MessageConstant;
import com.sky.result.Result;
import com.sky.utils.AliOssUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

/**
 * @author xuxunne
 * @description:
 * @since 2024/12/27 15:49
 */

@RestController
@RequestMapping("/admin/common")
@Api(tags = "common interface")
@Slf4j
public class CommonController {

    @Autowired
    private AliOssUtil aliOssUtil;

    @PostMapping("/upload")
    @ApiOperation("file upload")
    public Result<String> upload(MultipartFile file) {
        log.info("file upload");

        try {
        String originalFilename = file.getOriginalFilename();
        // to get the file suffix  (extension 扩展名)
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        // generate new file name
        String objectName = UUID.randomUUID().toString() + extension;
        // request path for file
            String filePath = aliOssUtil.upload(file.getBytes(), objectName);

            return  Result.success(filePath);
        } catch (IOException e) {
            log.error("File upload failed:{}",e);
        }

        return  Result.error(MessageConstant.UPLOAD_FAILED);

    }
}
