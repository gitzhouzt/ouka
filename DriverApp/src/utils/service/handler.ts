/** Unify the data types of failed and successful request results */
export async function handleServiceResult<T = any>(
	error: Service.RequestError | null,
	data: any
) {
	if (error) {
		const fail: Service.FailedResult = {
			error,
			data: null,
		};
		return fail;
	}
	const success: Service.SuccessResult<T> = {
		error: null,
		data,
	};
	return success;
}

type Adapter<T = any> = (...args: Service.RequestResult[]) => T;

/**
 * Data conversion adapter for request result
 * @param adapter - adapter function
 * @param args - Parameters of the adapter function
 */
export function serviceAdapter<T extends Adapter>(
	adapter: T,
	...args: TypeUtil.GetFunArgs<T>
) {
	let result: Service.RequestResult | undefined;

	const hasError = args.some((item) => {
		const flag = Boolean(item.error);
		if (flag) {
			result = {
				error: item.error,
				data: null,
			};
		}
		return flag;
	});

	if (!hasError) {
		result = {
			error: null,
			data: adapter(...args),
		};
	}

	return result as Service.RequestResult<TypeUtil.GetFunReturn<T>>;
}
