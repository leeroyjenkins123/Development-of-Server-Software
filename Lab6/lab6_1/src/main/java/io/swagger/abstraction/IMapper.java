package io.swagger.abstraction;

public interface IMapper<E, D> {
    D map(E entity);
    E reverseMap(D dto); // Опционально
}