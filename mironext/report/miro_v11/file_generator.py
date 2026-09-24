from __future__ import annotations

from pathlib import Path
from typing import Any


class MiroReportFileGenerator:
    """Stub for XHTML/template assembly during early Python porting."""

    def __init__(
        self,
        base_directory: str | Path,
        src_filename: str = "mirosource11.xhtml",
    ) -> None:
        self.base_directory = Path(base_directory)
        self.src_filename = src_filename

    def generate(
        self,
        pages: list[Any],
        variables: dict[str, str],
        img_names: dict[str, str],
    ) -> None:
        raise NotImplementedError(
            "MiroReportFileGenerator.generate is intentionally stubbed for now."
        )
