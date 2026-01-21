package sample.testcafekiosk.spring.api.service.product;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sample.testcafekiosk.spring.api.service.product.response.ProductResponse;
import sample.testcafekiosk.spring.domain.product.Product;
import sample.testcafekiosk.spring.domain.product.ProductRepository;
import sample.testcafekiosk.spring.domain.product.ProductSellingStatus;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public List<ProductResponse> getSellingProducts() {
        List<Product> products = productRepository.findAllBySellingStatusIn(ProductSellingStatus.getDisplay());

        return products.stream()
                .map(ProductResponse::of)
                .toList();
    }
}
