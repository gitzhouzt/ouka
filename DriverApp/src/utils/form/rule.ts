import type { Ref } from 'vue';
import {
	REGEXP_PWD,
	REGEXP_CODE_SIX,
	REGEXP_EMAIL,
	REGEXP_PHONE,
	REGEXP_POST,
} from '@/config';

/** form rules */
interface CustomFormRules {
	/** email */
	mail: any[];
	mail_option: any[];
	/** phone */
	phone: any[];
	phone_option: any[];
	/** password */
	pwd: any[];
	/** verification code */
	code: any[];
	/** post */
	post: any[];
}

/**  form rules */
export const formRules: CustomFormRules = {
	mail: [
		{ required: true, message: 'メールアドレスを入力してください' },
		{
			pattern: REGEXP_EMAIL,
			message: '有効なメールアドレスを入力してください',
			trigger: 'input',
		},
	],
	mail_option: [
		{ required: false, message: 'メールアドレスを入力してください' },
		{
			pattern: REGEXP_EMAIL,
			message: '有効なメールアドレスを入力してください',
			trigger: 'input',
		},
	],
	phone: [
		{ required: true, message: '電話番号を入力してください' },
		{
			pattern: REGEXP_PHONE,
			message: '半角数字電話番号を入力ください。',
			trigger: 'input',
		},
	],
	phone_option: [
		{ required: false, message: '電話番号を入力してください' },
		{
			pattern: REGEXP_PHONE,
			message: '半角数字電話番号を入力ください。',
			trigger: 'input',
		},
	],
	pwd: [
		{ required: true, message: 'パスワードを入力してください' },
		{ pattern: REGEXP_PWD, message: '半角英数記号8～16文字', trigger: 'input' },
	],
	code: [
		{ required: true, message: '確認コードを入力してください' },
		{
			pattern: REGEXP_CODE_SIX,
			message: '有効な確認コードを入力ください',
			trigger: 'input',
		},
	],
	post: [
		{ required: false, message: '郵便番号を入力してください' },
		{
			pattern: REGEXP_POST,
			message: '有効な郵便番号を入力ください',
			trigger: 'input',
		},
	],
};

/** Get form rule for confirm password */
export function getConfirmPwdRule(pwd: Ref<string>) {
	const confirmPwdRule: any[] = [
		{ required: true, message: '確認パスワードを入力してください' },
		{
			validator: (rule: any, value: any) => {
				if (!isBlankString(value) && value !== pwd.value) {
					return Promise.reject(rule.message);
				}
				return Promise.resolve();
			},
			message:
				'新しいパスワードが確認用に入力されたものと異なります。入力しなおしてください。',
			trigger: 'input',
		},
	];
	return confirmPwdRule;
}

/** Form rules for obtaining image verification code */
export function getImgCodeRule(imgCode: Ref<string>) {
	const imgCodeRule: any[] = [
		{ required: true, message: '確認コードを入力してください' },
		{
			validator: (rule: any, value: any) => {
				if (!isBlankString(value) && value !== imgCode.value) {
					return Promise.reject(rule.message);
				}
				return Promise.resolve();
			},
			message: '確認コードが間違い',
			trigger: 'blur',
		},
	];
	return imgCodeRule;
}

/** is it an empty string */
function isBlankString(str: string) {
	return str.trim() === '';
}
