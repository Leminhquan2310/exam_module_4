package com.exam_module_4.controller;

import com.exam_module_4.model.Product;
import com.exam_module_4.model.ProductCategory;
import com.exam_module_4.service.ProductCategoryService;
import com.exam_module_4.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Controller xử lý các request liên quan đến quản lý sản phẩm đấu giá
 * Bao gồm: hiển thị danh sách, thêm, sửa, xóa, tìm kiếm sản phẩm
 *
 * @author CodeGym
 * @version 1.0
 */
@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;
    @Autowired
    private ProductCategoryService productCategoryService;

    @GetMapping
    public String listProducts(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "5") int size,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "categoryId", required = false) Long categoryId,
            @RequestParam(name = "price", required = false) Double price,
            Model model
    ) {
        try {
            // Tạo đối tượng Pageable để phân trang
            Pageable pageable = PageRequest.of(page, size);
            Page<Product> productPage;

            // Kiểm tra có điều kiện tìm kiếm không
            boolean hasSearchCriteria = (name != null && !name.trim().isEmpty())
                    || categoryId != null
                    || price != null;

            if (hasSearchCriteria) {
                // Tìm kiếm với điều kiện
                productPage = productService.searchProducts(name, categoryId, price, pageable);

                // Lưu các tham số tìm kiếm để hiển thị lại trên form
                model.addAttribute("searchName", name);
                model.addAttribute("searchCategoryId", categoryId);
                model.addAttribute("searchPrice", price);
            } else {
                // Hiển thị tất cả sản phẩm
                productPage = productService.findAll(pageable);
            }

            // Lấy danh sách loại sản phẩm cho dropdown
            List<ProductCategory> categories = productCategoryService.findAll();

            // Thêm dữ liệu vào model
            model.addAttribute("products", productPage.getContent());
            model.addAttribute("categories", categories);
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", productPage.getTotalPages());
            model.addAttribute("totalItems", productPage.getTotalElements());
            model.addAttribute("pageSize", size);

            return "products/list";
        } catch (Exception e) {
            // Xử lý lỗi và hiển thị thông báo
            model.addAttribute("errorMessage", "Lỗi khi tải danh sách sản phẩm: " + e.getMessage());
            return "products/list";
        }
    }

    /**
     * Hiển thị form thêm sản phẩm mới
     *
     * @param model Model để truyền dữ liệu đến view
     * @return Tên view form thêm sản phẩm
     */
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        // Tạo đối tượng Product rỗng cho form
        Product product = new Product();

        // Lấy danh sách loại sản phẩm cho dropdown
        List<ProductCategory> categories = productCategoryService.findAll();

        // Thêm vào model
        model.addAttribute("product", product);
        model.addAttribute("categories", categories);

        return "products/create";
    }

    /**
     * Xử lý việc thêm sản phẩm mới
     *
     * @param product Đối tượng sản phẩm từ form (đã validate)
     * @param bindingResult Kết quả validation
     * @param model Model để truyền dữ liệu
     * @param redirectAttributes Attributes để truyền qua redirect
     * @return Redirect về danh sách nếu thành công, giữ lại form nếu có lỗi
     */
    @PostMapping("/create")
    public String createProduct(
            @Valid @ModelAttribute("product") Product product,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        // Kiểm tra lỗi validation
        if (bindingResult.hasErrors()) {
            // Nếu có lỗi, load lại danh sách loại sản phẩm và giữ lại form
            List<ProductCategory> categories = productCategoryService.findAll();
            model.addAttribute("categories", categories);
            return "products/create";
        }

        try {
            // Lưu sản phẩm vào database
            productService.save(product);

            // Thêm thông báo thành công
            redirectAttributes.addFlashAttribute("successMessage",
                    "Thêm sản phẩm '" + product.getName() + "' thành công!");

            // Redirect về trang danh sách
            return "redirect:/products";
        } catch (Exception e) {
            // Xử lý lỗi
            model.addAttribute("errorMessage", "Lỗi khi thêm sản phẩm: " + e.getMessage());

            // Load lại danh sách loại sản phẩm
            List<ProductCategory> categories = productCategoryService.findAll();
            model.addAttribute("categories", categories);

            return "products/create";
        }
    }

    /**
     * Hiển thị form sửa sản phẩm
     *
     * @param id ID của sản phẩm cần sửa
     * @param model Model để truyền dữ liệu
     * @param redirectAttributes Attributes để truyền qua redirect
     * @return Tên view form sửa hoặc redirect về danh sách nếu không tìm thấy
     */
    @GetMapping("/edit/{id}")
    public String showEditForm(
            @PathVariable("id") Long id,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        try {
            // Tìm sản phẩm theo ID
            Optional<Product> productOptional = productService.findById(id);

            if (productOptional.isPresent()) {
                // Nếu tìm thấy, load dữ liệu
                Product product = productOptional.get();
                List<ProductCategory> categories = productCategoryService.findAll();

                model.addAttribute("product", product);
                model.addAttribute("categories", categories);

                return "products/edit";
            } else {
                // Nếu không tìm thấy, thông báo lỗi và redirect
                redirectAttributes.addFlashAttribute("errorMessage",
                        "Không tìm thấy sản phẩm có ID: " + id);
                return "redirect:/products";
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Lỗi khi tải thông tin sản phẩm: " + e.getMessage());
            return "redirect:/products";
        }
    }

    /**
     * Xử lý việc cập nhật sản phẩm
     *
     * @param id ID của sản phẩm cần cập nhật
     * @param product Đối tượng sản phẩm từ form (đã validate)
     * @param bindingResult Kết quả validation
     * @param model Model để truyền dữ liệu
     * @param redirectAttributes Attributes để truyền qua redirect
     * @return Redirect về danh sách nếu thành công, giữ lại form nếu có lỗi
     */
    @PostMapping("/edit/{id}")
    public String updateProduct(
            @PathVariable("id") Long id,
            @Valid @ModelAttribute("product") Product product,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        // Kiểm tra lỗi validation
        if (bindingResult.hasErrors()) {
            // Nếu có lỗi, load lại danh sách loại sản phẩm
            List<ProductCategory> categories = productCategoryService.findAll();
            model.addAttribute("categories", categories);
            return "products/edit";
        }

        try {
            // Set ID cho product (đảm bảo update đúng record)
            product.setId(id);

            // Cập nhật vào database
            productService.save(product);

            // Thêm thông báo thành công
            redirectAttributes.addFlashAttribute("successMessage",
                    "Cập nhật sản phẩm '" + product.getName() + "' thành công!");

            return "redirect:/products";
        } catch (Exception e) {
            // Xử lý lỗi
            model.addAttribute("errorMessage", "Lỗi khi cập nhật sản phẩm: " + e.getMessage());

            // Load lại danh sách loại sản phẩm
            List<ProductCategory> categories = productCategoryService.findAll();
            model.addAttribute("categories", categories);

            return "products/edit";
        }
    }

    /**
     * Xử lý việc xóa nhiều sản phẩm
     *
     * @param selectedIds Danh sách ID các sản phẩm được chọn để xóa
     * @param redirectAttributes Attributes để truyền thông báo qua redirect
     * @return Redirect về trang danh sách
     */
    @PostMapping("/delete")
    public String deleteProducts(
            @RequestParam(value = "selectedIds", required = false) List<Long> selectedIds,
            RedirectAttributes redirectAttributes
    ) {
        // Kiểm tra có chọn sản phẩm nào không
        if (selectedIds == null || selectedIds.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Vui lòng chọn ít nhất một sản phẩm để xóa!");
            return "redirect:/products";
        }

        try {
            // Lấy thông tin các sản phẩm sẽ xóa để hiển thị tên
            List<Product> productsToDelete = productService.findAllById(selectedIds);

            // Tạo chuỗi tên các sản phẩm
            String productNames = productsToDelete.stream()
                    .map(Product::getName)
                    .collect(Collectors.joining(", "));

            // Thực hiện xóa
            productService.deleteByIds(selectedIds);

            // Thêm thông báo thành công với tên các sản phẩm đã xóa
            redirectAttributes.addFlashAttribute("successMessage",
                    "Đã xóa thành công " + selectedIds.size() + " sản phẩm: " + productNames);

            return "redirect:/products";
        } catch (Exception e) {
            // Xử lý lỗi
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Lỗi khi xóa sản phẩm: " + e.getMessage());
            return "redirect:/products";
        }
    }
}
