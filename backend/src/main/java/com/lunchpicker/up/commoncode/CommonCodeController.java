package com.lunchpicker.up.commoncode;

import com.lunchpicker.up.common.ApiResponse;
import com.lunchpicker.up.commoncode.dto.CommonCodeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/common-codes")
@RequiredArgsConstructor
public class CommonCodeController {

    private final CommonCodeService commonCodeService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<CommonCodeResponse>>> getCodes(
            @RequestParam(required = false) String group) {
        return ResponseEntity.ok(ApiResponse.ok(commonCodeService.getCodes(group)));
    }
}
