package akin.backend.table.service;

import akin.backend.table.dto.request.CreateTableRequest;
import akin.backend.table.dto.request.UpdateTableRequest;
import akin.backend.table.dto.response.TableResponse;
import akin.backend.table.entity.Masa;
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
        Masa masa = Masa.builder()
                .tableNumber(request.getTableNumber())
                .tableName(request.getTableName())
                .build();
        masa = tableRepository.save(masa);
        log.info("Masa oluşturuldu: {}", masa.getTableNumber());
        return toResponse(masa);
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
        Masa masa = tableRepository.findById(id).orElseThrow(TableNotFoundException::new);
        if (request.getTableNumber() != null) {
            if (!request.getTableNumber().equals(masa.getTableNumber()) && tableRepository.existsByTableNumber(request.getTableNumber())) {
                throw new DuplicateTableNumberException();
            }
            masa.setTableNumber(request.getTableNumber());
        }
        if (request.getTableName() != null) {
            masa.setTableName(request.getTableName());
        }
        masa = tableRepository.save(masa);
        log.info("Masa güncellendi: {}", masa.getTableNumber());
        return toResponse(masa);
    }

    @Override
    @Transactional
    public void delete(Long adminUserId, Long id) {
        ensureAdmin(adminUserId);
        Masa masa = tableRepository.findById(id).orElseThrow(TableNotFoundException::new);
        tableRepository.delete(masa);
        log.info("Masa silindi: {}", masa.getTableNumber());
    }

    @Override
    @Transactional(readOnly = true)
    public TableResponse getByTableNumber(String tableNumber) {
        Masa masa = tableRepository.findByTableNumber(tableNumber).orElseThrow(TableNotFoundException::new);
        return toResponse(masa);
    }

    private TableResponse toResponse(Masa masa) {
        return TableResponse.builder()
                .id(masa.getId())
                .tableNumber(masa.getTableNumber())
                .tableName(masa.getTableName())
                .build();
    }
}
