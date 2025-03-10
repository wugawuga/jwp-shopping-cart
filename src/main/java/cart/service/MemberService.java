package cart.service;

import cart.controller.dto.response.MemberResponse;
import cart.dao.MemberDao;
import cart.dao.MemberEntity;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberService {

    private final MemberDao memberDao;

    public MemberService(final MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    @Transactional(readOnly = true)
    public List<MemberResponse> findAll() {
        List<MemberEntity> findMembers = memberDao.findAll();

        return findMembers.stream()
                .map(entity -> new MemberResponse(entity.getId(), entity.getEmail(), entity.getPassword()))
                .collect(Collectors.toList());
    }

}
