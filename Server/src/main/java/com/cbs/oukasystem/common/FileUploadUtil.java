package com.cbs.oukasystem.common;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.AclEntry;
import java.nio.file.attribute.AclEntryPermission;
import java.nio.file.attribute.AclEntryType;
import java.nio.file.attribute.AclFileAttributeView;
import java.nio.file.attribute.GroupPrincipal;
import java.nio.file.attribute.UserPrincipalLookupService;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cbs.oukasystem.common.MessageEnum.EnumUploadCheck;
import com.cbs.oukasystem.config.BaseException;

/**
 * 写真アップロード共通
 */
@Service
@PropertySource("classpath:application.properties")
@ConfigurationProperties(prefix = "cbs")
public class FileUploadUtil {

    public static String uploadPath;

    public static String serverUrl;

    @Value("${cbs.resource.uploadPath}")
    public void setUploadPath(String uploadPath) {
        FileUploadUtil.uploadPath = uploadPath;
    }

    @Value("${cbs.server.url}")
    public void setServerUrl(String serverUrl) {
        FileUploadUtil.serverUrl = serverUrl;
    }

    /**
     * 写真拡張子をチェック
     * 
     * @param files 写真
     * @return 拡張子が存在するかどうか
     */
    public static Boolean checkPhoto(MultipartFile... files) {

        if (files != null && files.length > 0) {
            for (MultipartFile fileItem : files) {
                // 写真名
                String fileName = fileItem.getOriginalFilename();
                // 写真の拡張子名
                String suffixName = fileName.substring(fileName.lastIndexOf("."));
                List<String> extList = Arrays.asList(".jpg", ".png", ".jpeg", ".gif", ".webp");
                if (!extList.contains(suffixName)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * ファイル拡張子をチェック
     * 
     * @param files ファイル
     * @return 拡張子が存在するかどうか
     */
    public static Boolean checkFile(MultipartFile... files) {

        if (files != null && files.length > 0) {
            for (MultipartFile fileItem : files) {
                // ファイル名
                String fileName = fileItem.getOriginalFilename();
                // ファイルの拡張子名
                String suffixName = fileName.substring(fileName.lastIndexOf("."));
                List<String> extList = Arrays.asList(".pdf", ".text", ".xls", ".xlsx", ".doc", ".docx");
                if (!extList.contains(suffixName)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * ファイル拡張子をチェック
     * 
     * @param files ファイル
     * @return suffixName
     */
    public static String getSuffixName(MultipartFile... files) {
        String suffixName = "";
        if (files != null && files.length > 0) {
            for (MultipartFile fileItem : files) {
                // ファイル名
                String fileName = fileItem.getOriginalFilename();
                // ファイルの拡張子名
                suffixName = fileName.substring(fileName.lastIndexOf("."));
            }
        }
        return suffixName;
    }

    /**
     * ファイルをアップロード処理。
     * 
     * @param key    区分Key()
     * @param userId ユーザーID(ユーザーID値、会社ID値、ドライバーID値)
     * @param files  ファイル
     * @return アップロードパス
     */
    public static String upload(String key, String userId, MultipartFile... files) {

        String filePath = "";
        Date date = new Date();
        String dateForm = new SimpleDateFormat("yyyy-MM-dd").format(date);
        // URLを設定
        String url = "";
        String keyPath = (null == key || key.isEmpty()) ? "" : "/" + key;
        if (null != userId && !userId.isEmpty()) {
            url = keyPath + "/" + userId + "/" + dateForm + "/";
        } else {
            url = keyPath + "/" + dateForm + "/";
        }
        // ファイルをアップロードする。
        if (files.length > 0) {
            for (MultipartFile fileItem : files) {
                if (null != filePath && filePath.isEmpty()) {
                    filePath = upload(fileItem, url);
                } else {
                    filePath = filePath + "|" + upload(fileItem, url);
                }
            }
        }
        return filePath;
    }

    /**
     * アップロード処理。
     * 
     * @param file
     * @param filePath アップロードパス設定
     * @return アップロードパス
     */
    private static String upload(MultipartFile file, String filePath) {
        // ファイル名
        String fileName = file.getOriginalFilename();
        // ファイルの拡張子名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        fileName = UUID.randomUUID().toString().replace("-", "") + suffixName;
        String url = filePath + fileName;
        File dest = new File(uploadPath + url);
        // ファイルの存在をチェック
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        try {
            file.transferTo(dest);

            // ファイル権限
            Path path = Paths.get(dest.getPath());
            AclFileAttributeView aView = Files.getFileAttributeView(path, AclFileAttributeView.class);
            UserPrincipalLookupService lookupService = FileSystems.getDefault().getUserPrincipalLookupService();
            GroupPrincipal groupPrincipal = lookupService.lookupPrincipalByGroupName("Everyone");
            AclEntry.Builder builder = AclEntry.newBuilder();
            builder.setPrincipal(groupPrincipal);
            builder.setType(AclEntryType.ALLOW);

            List<AclEntry> aclEntries = aView.getAcl();
            Set<AclEntryPermission> permissions = aclEntries.get(0).permissions();

            builder.setPermissions(permissions);
            AclEntry newEntry = builder.build();
            aclEntries.add(newEntry);
            aView.setAcl(aclEntries);

        } catch (IllegalStateException | IOException e) {
            throw new BaseException(EnumUploadCheck.ERROR, e.getMessage());
        }
        return url;
    }

}