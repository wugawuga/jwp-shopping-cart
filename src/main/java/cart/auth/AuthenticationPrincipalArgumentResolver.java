package cart.auth;

import cart.dao.MemberDao;
import cart.dao.MemberEntity;
import cart.domain.Member;
import cart.exception.AuthException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class AuthenticationPrincipalArgumentResolver implements HandlerMethodArgumentResolver {

    private final MemberDao memberDao;

    public AuthenticationPrincipalArgumentResolver(final MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    private static final String DELIMITER = ":";
    private static final String AUTHENTICATION_HEADER_NAME = "Basic";
    private static final String EMAIL_REGX = "^(.+)@(.+)$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGX);

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthenticationPrincipal.class);
    }

    @Override
    public Integer resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                   NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        String header = getValidateHeader(webRequest);
        String basicHeaderValue = header.substring(AUTHENTICATION_HEADER_NAME.length()).trim();
        byte[] decodedHeaderValue = Base64.decodeBase64(basicHeaderValue);

        String decodedStringHeaderValue = new String(decodedHeaderValue);
        validateDecodedHeaderValue(decodedStringHeaderValue);
        String[] splitDecodedStringHeaderValue = decodedStringHeaderValue.split(DELIMITER);
        validateEmail(splitDecodedStringHeaderValue[1]);
        int id = Integer.parseInt(splitDecodedStringHeaderValue[0]);
        String email = splitDecodedStringHeaderValue[1];
        String password = splitDecodedStringHeaderValue[2];

        try {
            MemberEntity findMemberEntity = memberDao.findMemberById(id)
                    .orElseThrow(() -> new IllegalArgumentException("찾을 수 없는 인증 정보입니다."));

            Member member = toMember(findMemberEntity);
            member.checkEmail(email);
            member.checkPassword(password);
        } catch (IllegalArgumentException exception) {
            throw new AuthException(exception.getMessage());
        }

        return id;
    }

    private void validateEmail(final String email) {
        Matcher matcher = EMAIL_PATTERN.matcher(email);
        if (!matcher.matches()) {
            throw new AuthException("유효하지 않은 이메일입니다.");
        }
    }

    private void validateDecodedHeaderValue(final String decodedStringHeaderValue) {
        try {
            if (!decodedStringHeaderValue.contains(DELIMITER)) {
                throw new AuthException("잘못된 인증 정보입니다.");
            }
            String[] decodedSplitHeaderValue = decodedStringHeaderValue.split(DELIMITER);
            Integer.parseInt(decodedSplitHeaderValue[0]);
            validateDecodedSplitHeaderValueSize(decodedSplitHeaderValue);
        } catch (NumberFormatException exception) {
            throw new AuthException("잘못된 인증 정보입니다.");
        }
    }

    private void validateDecodedSplitHeaderValueSize(final String[] decodedSplitHeaderValue) {
        if (decodedSplitHeaderValue.length != 3) {
            throw new AuthException("잘못된 인증 정보입니다.");
        }
    }

    private String getValidateHeader(final NativeWebRequest webRequest) {
        String authorizationHeader = webRequest.getHeader("Authorization");
        validateHasAuthorizationHeader(authorizationHeader);
        String basicHeaderValue = authorizationHeader.substring("Basic".length()).trim();
        validateHasBasicHeaderValue(basicHeaderValue);
        return authorizationHeader;
    }

    private void validateHasBasicHeaderValue(final String basicHeaderValue) {
        if (basicHeaderValue == null) {
            throw new AuthException("Basic 인증 정보가 없습니다.");
        }
    }

    private void validateHasAuthorizationHeader(final String authorizationHeader) {
        if (authorizationHeader == null) {
            throw new AuthException("인증 정보가 없습니다");
        }
        if (!authorizationHeader.contains(AUTHENTICATION_HEADER_NAME)) {
            throw new AuthException("인증 정보가 없습니다");
        }
    }

    private Member toMember(final MemberEntity findMemberEntity) {
        return new Member(findMemberEntity.getId(), findMemberEntity.getEmail(), findMemberEntity.getPassword());
    }

}
