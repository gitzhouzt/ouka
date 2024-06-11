import { NO_ERROR_MSG_CODE, ERROR_MSG_DURATION } from '@/config';

/** Error message stack to prevent the same error from appearing at the same time */
const errorMsgStack = new Map<string | number, string>([]);

function addErrorMsg(error: Service.RequestError) {
  errorMsgStack.set(error.code, error.msg);
}
function removeErrorMsg(error: Service.RequestError) {
  errorMsgStack.delete(error.code);
}
function hasErrorMsg(error: Service.RequestError) {
  return errorMsgStack.has(error.code);
}

/**
 * show error message
 * @param error
 */
export function showErrorMsg(error: Service.RequestError) {
  if (!error.msg) return;
  if (!NO_ERROR_MSG_CODE.includes(error.code)) {
    if (!hasErrorMsg(error)) {
      addErrorMsg(error);
      window.console.warn(error.code, error.msg);
      window.$message?.error(error.msg, { duration: ERROR_MSG_DURATION });
      setTimeout(() => {
        removeErrorMsg(error);
      }, ERROR_MSG_DURATION);
    }
  }
}
