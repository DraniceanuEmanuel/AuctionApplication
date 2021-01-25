package com.sda.remote2.controller;

import com.sda.remote2.dto.BidDto;
import com.sda.remote2.dto.ProductDto;
import com.sda.remote2.dto.UserDto;
import com.sda.remote2.service.BidService;
import com.sda.remote2.service.ProductService;
import com.sda.remote2.service.UserService;
import com.sda.remote2.validator.BidValidator;
import com.sda.remote2.validator.UserDtoValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
public class MainController {

    private UserService userService;
    private UserDtoValidator userDtoValidator;
    private ProductService productService;
    private BidService bidService;
    private BidValidator bidValidator;

    @Autowired
    public MainController(UserService userService,
                          UserDtoValidator userDtoValidator,
                          ProductService productService,
                          BidService bidService,
                          BidValidator bidValidator){
        this.userService = userService;
        this.userDtoValidator = userDtoValidator;
        this.productService = productService;
        this.bidService = bidService;
        this.bidValidator = bidValidator;
    }

    @GetMapping({"/home", "/"})
    public String home(Model model) {
        List<ProductDto> products = productService.getAllActiveProducts();
        model.addAttribute("products", products);
        return "home";
    }

    @GetMapping("/registration")
    public String registrationGET(Model model) {
        model.addAttribute("userDto", new UserDto());
        return "registration";
    }

    @PostMapping("/registration")
    public String registrationPOST(Model model, UserDto userDto, BindingResult bindingResult) {
        userDtoValidator.validate(userDto, bindingResult);
        if(bindingResult.hasErrors()){
            return "registration";
        }
        userService.register(userDto);
        return "redirect:/home";
    }

    @GetMapping("/addProduct")
    public String addProductGET(Model model) {
        model.addAttribute("productDto", new ProductDto());
        return "add-product";
    }
    @PostMapping("/addProduct")
    public String addProductPOST(Model model, ProductDto productDto, BindingResult bindingResult,
                                 @RequestParam("productImage") MultipartFile multipartFile, Authentication authentication){
        String userEmail = authentication.getName();
        productService.addProduct(productDto, multipartFile, userEmail);
        return "redirect:/home";
    }

    @GetMapping("/product/{productId}")
    public String viewProductGet(@PathVariable(value="productId") String productId, Model model, Authentication authentication){
        ProductDto productDto = productService.getProductById(productId, authentication.getName());
        model.addAttribute("productDto", productDto);
        model.addAttribute("bidDto", new BidDto());
        return "view-product";
    }

    @PostMapping("/product/{productId}")
    public String viewProductPost(Model model,
                                  BidDto bidDto,
                                  BindingResult bindingResult,
                                  @PathVariable(value="productId") String productId,
                                  Authentication authentication){
        bidValidator.validate(bidDto, productId, bindingResult);
        if(bindingResult.hasErrors()) {
            ProductDto productDto = productService.getProductById(productId, productId);
            model.addAttribute("productDto", productDto);
            return "view-product";
        }
        bidService.addBid(bidDto, productId, authentication.getName());
        return "redirect:/home";
    }

    @GetMapping({"/myProducts"})
    public String myProductsGET(Model model, Authentication authentication) {
        List<ProductDto> products = productService.getProductsFor(authentication.getName());
        model.addAttribute("products", products);
        return "my-product";
    }

    @GetMapping({"/myBids"})
    public String myBidsGET(Model model, Authentication authentication) {
        List<ProductDto> products = productService.getProductsBiddedBy(authentication.getName());
        model.addAttribute("products", products);
        return "my-bids";
    }
}
