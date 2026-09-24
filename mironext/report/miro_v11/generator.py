from __future__ import annotations

import json
from pathlib import Path

from .miro_report import MiroReport
from .models import MiroV11ReportInput


def load_report_input(path: str | Path) -> MiroV11ReportInput:
    payload = json.loads(Path(path).read_text(encoding="utf-8"))
    return MiroV11ReportInput(**payload)


def generate_report_v11(
    report_input: MiroV11ReportInput,
    output_dir: str | Path,
) -> Path:
    output_path = Path(output_dir)
    output_path.mkdir(parents=True, exist_ok=True)

    report = MiroReport(base_directory=output_path)
    report.generate_report_v11(report_input)

    return output_path / f"{report_input.report_name}.pdf"
