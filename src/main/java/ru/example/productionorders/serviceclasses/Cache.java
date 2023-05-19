package ru.example.productionorders.serviceclasses;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Service;
import ru.example.productionorders.classes.Categorie;
import ru.example.productionorders.classes.Supplier;
import ru.example.productionorders.configuration.ApplicationContextSingleton;
import ru.example.productionorders.repositories.CacheRepository;

import java.util.HashMap;
import java.util.Map;

@Service
@Getter
@Setter
@Slf4j
public class Cache {

    private Map<String, Supplier> supplierMap = new HashMap<>();
    private Map<String, Categorie> categorieMap = new HashMap<>();
    private Integer productMaxId;
    private Integer shipperMaxId;

    public void initCache() {
        AnnotationConfigApplicationContext context = ApplicationContextSingleton.getContext();
        CacheRepository cacheRepository = context.getBean(CacheRepository.class);
        supplierMap = cacheRepository.getSuppliers();
        categorieMap = cacheRepository.getCategories();
        productMaxId = cacheRepository.getMaxProductId();
        shipperMaxId = cacheRepository.getMaxShipperId();
    }
}
