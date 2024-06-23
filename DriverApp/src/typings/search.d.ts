declare namespace MySearch {
	interface SearchParams {
		keyword?: string;
		page?: number;
		pageSize?: number;
		beginTime?: Date;
		endTime?: Date;
		isAudit?: boolean;
	}

	interface AccessSearchParams extends SearchParams {
		supportStatus: string;
		accessSort: string;
	}

	interface LogSearchParams extends SearchParams {
		logType: string;
	}
}
