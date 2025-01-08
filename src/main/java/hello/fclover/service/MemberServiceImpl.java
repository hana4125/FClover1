package hello.fclover.service;

import hello.fclover.domain.AddressBook;
import hello.fclover.domain.Member;
import hello.fclover.domain.Notice;
import hello.fclover.mybatis.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberMapper dao;
    private static final String UPLOAD_DIR = "src/main/resources/static/img/user/upload/";

    @Override
    public int signup(Member member) {
        return dao.insertMember(member);
    }

    @Override
    public int getMemNum(String memberId) {
        return dao.selectMemNum(memberId);
    }

    @Override
    public Member findMemberById(String id) {
        return dao.selectMemberById(id);
    }

    @Override
    public int updateMember(Member member) {
        return dao.updateMember(member);
    }

    @Override
    public int updateSocialMember(Member member) {
        return dao.updateSocialMember(member);
    }

    @Override
    public int updateProfile(Member member) {
        return dao.updateProfile(member);
    }

    @Override
    public int removeProfilePicture(String memberId) {
        return dao.deleteProfilePicture(memberId);
    }

    @Override
    public int addDeliveryAddress(AddressBook addressBook) {
        return dao.insertAddressBook(addressBook);
    }

    @Override
    @Transactional
    public void setDefaultAddress(int addressId) {
        dao.updateDefaultAddress(addressId);
    }

    @Override
    public List<AddressBook> getDeliveryAddress(int memNum) {
        return dao.selectAddressBook(memNum);
    }

    @Override
    public Member isMemberExists(String memberId, String password) {
        return dao.selectMember(memberId, password);
    }

    @Override
    public String getEncryptedPassword(String memberId) {
        return dao.selectPassword(memberId);
    }

    @Override
    public void removeAccount(String memberId) {
        dao.deleteMember(memberId);
    }

    @Override
    public AddressBook getDefaultAddress(int memNum) {
        return dao.selectDefaultAddress(memNum);
    }

    @Override
    public int checkDefaultAddress(int addressNum) {
        return dao.selectIsDefault(addressNum);
    }

    @Override
    public int removeAddressBook(int addressNum) {
        return dao.deleteAddressBook(addressNum);
    }

    @Override
    public String uploadProfilePicture(MultipartFile file, String memberId) throws IOException {

        if (file.isEmpty()) {
            throw new IllegalArgumentException("파일이 비어 있습니다.");
        }

        if (!file.getContentType().startsWith("image/")) {
            throw new IllegalArgumentException("이미지 파일만 업로드할 수 있습니다.");
        }

        String fileName = file.getOriginalFilename();
        Path filePath = Paths.get(UPLOAD_DIR + fileName);
        Files.createDirectories(filePath.getParent()); // 폴더 생성
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        Member member = dao.selectMemberById(memberId);
        if (member == null) {
            throw new IllegalArgumentException("존재하지 않는 사용자입니다.");
        }

        member.setProfilePicture(fileName);
        int result = dao.updateProfile(member);

        if (result != 1) {
            throw new IllegalArgumentException("프로필 업로드 실패");
        }

        return member.getProfilePicture();
    }




    @Override
    public void setReadCountUpdate(int num) {
        dao.setReadCountUpdate(num);
    }

    @Override
    public Member getDetail(int num) {
        return dao.getDetail(num);
    }


}
