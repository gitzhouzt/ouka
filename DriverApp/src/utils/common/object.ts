/** set objet data */
export function objectAssign<T extends Record<string, any>>(
	target: T,
	source: Partial<T>
) {
	Object.assign(target, source);
}
