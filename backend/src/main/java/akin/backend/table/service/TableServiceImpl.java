package akin.backend.table.service;

import akin.backend.table.dto.request.CreateTableRequest;
import akin.backend.table.dto.request.UpdateTableRequest;
import akin.backend.table.dto.response.TableResponse;
import akin.backend.table.entity.Table;
import akin.backend.table.exception.DuplicateTableNumberException;
import akin.backend.table.exception.TableNotFoundException;
import akin.backend.table.repository.TableRepository;
import akin.backend.user.entity.Role;
import akin.backend.user.entity.User;
import akin.backend.user.exception.UserNotFoundException;
import akin.backend.user.exception.UserNotAdminException;
import akin.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TableServiceImpl implements TableService {

    private final TableRepository tableRepository;
    private final UserRepository userRepository;

    private void ensureAdmin(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        if (user.getRole() != Role.ADMIN) {
            throw new UserNotAdminException();
        }
    }

    @Override
    @Transactional
    public TableResponse create(Long adminUserId, CreateTableRequest request) {
        ensureAdmin(adminUserId);
        if (tableRepository.existsByTableNumber(request.getTableNumber())) {
            throw new DuplicateTableNumberException();
        }
        Table table = Table.builder()
                .tableNumber(request.getTableNumber())
                .tableName(request.getTableName())
                .build();
        table = tableRepository.save(table);
        log.info("Masa oluşturuldu: {}", table.getTableNumber());
        return toResponse(table);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TableResponse> findAll(Long adminUserId) {
        ensureAdmin(adminUserId);
        return tableRepository.findAllByOrderByTableNumberAsc().stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public TableResponse update(Long adminUserId, Long id, UpdateTableRequest request) {
        ensureAdmin(adminUserId);
        Table table = tableRepository.findById(id).orElseThrow(TableNotFoundException::new);
        if (request.getTableNumber() != null) {
            if (!request.getTableNumber().equals(table.getTableNumber()) && tableRepository.existsByTableNumber(request.getTableNumber())) {
                throw new DuplicateTableNumberException();
            }
            table.setTableNumber(request.getTableNumber());
        }
        if (request.getTableName() != null) {
            table.setTableName(request.getTableName());
        }
        table = tableRepository.save(table);
        log.info("Masa güncellendi: {}", table.getTableNumber());
        return toResponse(table);
    }

    @Override
    @Transactional
    public void delete(Long adminUserId, Long id) {
        ensureAdmin(adminUserId);
        Table table = tableRepository.findById(id).orElseThrow(TableNotFoundException::new);
        tableRepository.delete(table);
        log.info("Masa silindi: {}", table.getTableNumber());
    }

    @Override
    @Transactional(readOnly = true)
    public TableResponse getByTableNumber(String tableNumber) {
        Table table = tableRepository.findByTableNumber(tableNumber).orElseThrow(TableNotFoundException::new);
        return toResponse(table);
    }

    private TableResponse toResponse(Table table) {
        return TableResponse.builder()
                .id(table.getId())
                .tableNumber(table.getTableNumber())
                .tableName(table.getTableName())
                .build();
    }
}
