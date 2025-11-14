package com.ms.e_project.Services.admin.faq;

import com.ms.e_project.dto.FAQDto;

public interface FAQService {
    FAQDto postFAQ(Long productId, FAQDto faqDto);
}
