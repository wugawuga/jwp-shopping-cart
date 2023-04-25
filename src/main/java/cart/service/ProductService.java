package cart.service;

import cart.dao.ProductDao;
import cart.dao.ProductEntity;
import cart.dto.ProductDto;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private final ProductDao productDao;

    public ProductService(final ProductDao productDao) {
        this.productDao = productDao;
    }

    public List<ProductEntity> findAll() {
        return productDao.findAll();
    }

    public void insert(final ProductDto productDto) {
        ProductEntity productEntity = new ProductEntity(productDto.getName(), productDto.getImage(),
                productDto.getPrice());
        productDao.insert(productEntity);
    }

    public void update(final int id, final ProductDto productDto) {
        ProductEntity productEntity = productDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("상품 id를 확인해주세요."));

        ProductEntity updatedEntity = productEntity.update(productDto);

        productDao.update(updatedEntity);

    }

    public void delete(final int id) {
        ProductEntity productEntity = productDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("상품 id를 확인해주세요."));

        productDao.delete(id);
    }
}
