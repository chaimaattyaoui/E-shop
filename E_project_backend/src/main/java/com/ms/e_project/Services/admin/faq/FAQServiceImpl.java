package com.ms.e_project.Services.admin.faq;

import com.ms.e_project.Entities.FAQ;
import com.ms.e_project.Entities.Product;
import com.ms.e_project.Repository.FAQRepository;
import com.ms.e_project.Repository.ProductRepository;
import com.ms.e_project.dto.FAQDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FAQServiceImpl implements FAQService {

    private final FAQRepository faqRepository;
    private final ProductRepository productRepository;

    public FAQDto postFAQ(Long productId, FAQDto faqDto) {
        Optional<Product> optionalProduct = productRepository.findById(productId);

        if (optionalProduct.isEmpty() ||
                faqDto.getQuestion() == null || faqDto.getQuestion().trim().isEmpty() ||
                faqDto.getAnswer() == null || faqDto.getAnswer().trim().isEmpty()) {
            throw new IllegalArgumentException("Question and answer cannot be empty");
        }

        FAQ faq = new FAQ();
        faq.setQuestion(faqDto.getQuestion());
        faq.setAnswer(faqDto.getAnswer());
        faq.setProduct(optionalProduct.get());

        return faqRepository.save(faq).getFAQDto();
    }

}
