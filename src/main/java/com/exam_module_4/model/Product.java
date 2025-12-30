package com.exam_module_4.model;


import jakarta.persistence.*;
import lombok.*;

import jakarta.validation.constraints.*;

@Entity
@Table(name = "san_pham")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 200)
    @NotBlank(message = "Tên sản phẩm không được để trống")
    @Size(min = 5, max = 50, message = "Tên sản phẩm phải từ 5 đến 50 ký tự")
    private String name;

    @Column(name = "price", nullable = false)
    @NotNull(message = "Giá khởi điểm không được để trống")
    @Min(value = 100000, message = "Giá khởi điểm phải lớn hơn hoặc bằng 100,000 VND")
    private Double price;

    @Column(name = "status", nullable = false, length = 100)
    @NotBlank(message = "Tình trạng không được để trống")
    private String status;

    @ManyToOne
    @JoinColumn(name = "id_loai_sp", nullable = false)
    @NotNull(message = "Loại sản phẩm không được để trống")
    private ProductCategory category;
}
