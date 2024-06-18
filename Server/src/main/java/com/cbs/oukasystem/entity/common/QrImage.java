package com.cbs.oukasystem.entity.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QrImage {

    private String qrCodeFileOutputPath;

    private String qrCodeContent;

    private int qrCodeWidth;

    private int qrCodeHeight;

    private String embeddedImgFilePath;

    private int wordSize;

    private String wordContent;

}