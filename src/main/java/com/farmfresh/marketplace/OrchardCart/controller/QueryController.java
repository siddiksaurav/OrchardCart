package com.farmfresh.marketplace.OrchardCart.controller;

import com.farmfresh.marketplace.OrchardCart.dto.request.ProductQuery;
import com.farmfresh.marketplace.OrchardCart.service.QueryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/query")
public class QueryController {
    @Autowired
    private QueryService queryService;
    @PostMapping ("/products/{id}")
    public String QueryOverEmail(@PathVariable Long id, @Valid @RequestBody ProductQuery productQuery){
        return queryService.sendQuery(id,productQuery);
   }
}
