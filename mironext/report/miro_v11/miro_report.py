from __future__ import annotations

from pathlib import Path
from typing import Any

from .file_generator import MiroReportFileGenerator


class MiroReport:
    """Skeleton Python port of Java MiroReport."""

    def __init__(
        self,
        base_directory: str | Path,
        engaged_score: int = 0,
        excess_score: int = 0,
        latent_score: int = 0,
        report_file_generator: MiroReportFileGenerator | None = None,
    ) -> None:
        self.base_directory = Path(base_directory)
        self.engaged_score = engaged_score
        self.excess_score = excess_score
        self.latent_score = latent_score
        self.report_file_generator = report_file_generator or MiroReportFileGenerator(
            self.base_directory
        )
        self.miro_response: Any | None = None

    def setup_response(self, miro_response: Any) -> None:
        """Attach response payload for later generation steps."""
        self.miro_response = miro_response

    def generate_report_v11(self, miro_response: Any) -> bool:
        """Placeholder entry point for MiRo V11 report generation."""
        self.setup_response(miro_response)
        raise NotImplementedError(
            "MiroReport.generate_report_v11 is not implemented yet."
        )

    def generate_report_v10(self, miro_response: Any) -> bool:
        """Placeholder entry point for MiRo V10 report generation."""
        self.setup_response(miro_response)
        raise NotImplementedError(
            "MiroReport.generate_report_v10 is not implemented yet."
        )

    def generate_report_leadership01(self, miro_response: Any) -> bool:
        """Placeholder entry point for leadership report generation."""
        self.setup_response(miro_response)
        raise NotImplementedError(
            "MiroReport.generate_report_leadership01 is not implemented yet."
        )

    def get_report_file_path(self, report_name: str, version: int | str) -> Path:
        """Build report PDF path using the Java out-folder convention."""
        return self.base_directory / "out" / f"{report_name}_{version}.pdf"

    # Java-style aliases retained to ease incremental porting.
    def generateReportV11(self, miro_response: Any) -> bool:  # noqa: N802
        return self.generate_report_v11(miro_response)

    def generateReportV10(self, miro_response: Any) -> bool:  # noqa: N802
        return self.generate_report_v10(miro_response)

    def generateReportLeadership01(self, miro_response: Any) -> bool:  # noqa: N802
        return self.generate_report_leadership01(miro_response)

    def getReportFilePath(self, report_name: str, version: int | str) -> Path:  # noqa: N802
        return self.get_report_file_path(report_name, version)
