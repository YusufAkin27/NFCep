package akin.backend.admin.service;

import akin.backend.admin.request.ChangePasswordRequest;
import akin.backend.admin.request.CreateMutfakRequest;
import akin.backend.admin.request.CreateWaiterRequest;
import akin.backend.admin.request.SetWorkingTodayRequest;
import akin.backend.admin.response.EmployeeStatisticsResponse;
import akin.backend.user.dto.response.UserResponse;

import java.util.List;

public interface AdminService {

    UserResponse createWaiter(Long adminUserId, CreateWaiterRequest request);

    List<UserResponse> getAllGarsons(Long adminUserId);

    void changeGarsonPassword(Long adminUserId, Long garsonId, ChangePasswordRequest request);

    void deleteGarson(Long adminUserId, Long garsonId);

    void setGarsonWorkingToday(Long adminUserId, Long garsonId, SetWorkingTodayRequest request);

    UserResponse createMutfak(Long adminUserId, CreateMutfakRequest request);

    List<UserResponse> getAllMutfak(Long adminUserId);

    void changeMutfakPassword(Long adminUserId, Long mutfakId, ChangePasswordRequest request);

    void deleteMutfak(Long adminUserId, Long mutfakId);

    List<EmployeeStatisticsResponse> getEmployeeStatistics(Long adminUserId);
}
