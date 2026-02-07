package akin.backend.table.service;

import akin.backend.table.dto.request.CreateTableRequest;
import akin.backend.table.dto.request.UpdateTableRequest;
import akin.backend.table.dto.response.TableResponse;

import java.util.List;

public interface TableService {

    TableResponse create(Long adminUserId, CreateTableRequest request);

    List<TableResponse> findAll(Long adminUserId);

    TableResponse update(Long adminUserId, Long id, UpdateTableRequest request);

    void delete(Long adminUserId, Long id);

    TableResponse getByTableNumber(String tableNumber);
}
