function readNumber(value, fallback) {
  const n = Number.parseInt(value, 10);
  return Number.isFinite(n) ? n : fallback;
}

export function getPagination(searchParams, options = {}) {
  const { defaultPage = 1, pageSize = 10, maxPageSize = 50 } = options;

  const pageRaw = Array.isArray(searchParams?.page)
    ? searchParams.page[0]
    : searchParams?.page;
  const sizeRaw = Array.isArray(searchParams?.pageSize)
    ? searchParams.pageSize[0]
    : searchParams?.pageSize;

  const page = Math.max(1, readNumber(pageRaw, defaultPage));
  const requestedPageSize = Math.max(1, readNumber(sizeRaw, pageSize));
  const safePageSize = Math.min(maxPageSize, requestedPageSize);

  return {
    page,
    pageSize: safePageSize,
    skip: (page - 1) * safePageSize,
    take: safePageSize,
  };
}
