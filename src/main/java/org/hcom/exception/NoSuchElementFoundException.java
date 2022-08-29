package org.hcom.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 에러 응답의 내용을 수정할 수 없음
 * 예외 상황마다 예외 클래스를 추가해야 함
 * 예외 클래스와 강하게 결합되어 모든 예외에 대해 동일한 상태와 에러 메세지를 반환하게 됨
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class NoSuchElementFoundException extends RuntimeException {

}
