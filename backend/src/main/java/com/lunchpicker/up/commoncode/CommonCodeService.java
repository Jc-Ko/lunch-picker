package com.lunchpicker.up.commoncode;

import com.lunchpicker.up.commoncode.dto.CommonCodeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommonCodeService {

    private final CommonCodeRepository commonCodeRepository;

    public List<CommonCodeResponse> getCodes(String group) {
        List<CommonCode> codes = (group != null && !group.isBlank())
                ? commonCodeRepository.findByCodeGroupOrderBySortOrderAsc(group)
                : commonCodeRepository.findAllByOrderBySortOrderAsc();

        return codes.stream()
                .map(c -> new CommonCodeResponse(c.getId(), c.getCodeGroup(), c.getCode(), c.getLabel(), c.getSortOrder()))
                .toList();
    }
}
