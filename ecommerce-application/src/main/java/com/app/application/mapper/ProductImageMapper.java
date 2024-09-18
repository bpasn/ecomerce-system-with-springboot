package com.app.application.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.app.application.dto.ProductImageDTO;
import com.app.domain.entity.ProductImageEntity;

@Mapper(componentModel = "spring")
public interface ProductImageMapper extends BaseMapper<ProductImageDTO, ProductImageEntity> {

    ProductImageMapper INSTANCE = Mappers.getMapper(ProductImageMapper.class);

    @Override
    @Mapping(target = "uri", source = "source")
    ProductImageDTO toDTO(ProductImageEntity entity);

    @Override
    @Mapping(target = "source", source = "uri")
    ProductImageEntity toEntity(ProductImageDTO dto);

    // @Named("toStringDTO")
    // default MultipartFile toStringDTO(String source) {
    //     if (filePath == null) {
    //         return null;
    //     }
    //     try {
    //         Path path = Paths.get(filePath);
    //         String name = path.getFileName().toString();
    //         String contentType = Files.probeContentType(path);
    //         byte[] content = Files.readAllBytes(path);
    //         return new CustomMultipartFile(content, name, name, contentType);
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //         return null;
    //     }
    // }

    // @Named("multipartFileToString")
    // default String multipartFileToString(MultipartFile file) {
    //     return file != null ? file.getOriginalFilename() : null; // Mapping file name as String
    // }
}
