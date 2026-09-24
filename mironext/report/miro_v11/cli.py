from __future__ import annotations

import argparse

from .generator import generate_report_v11, load_report_input


def main() -> None:
    parser = argparse.ArgumentParser(description="Generate MiRo V11 PDF using ReportLab.")
    parser.add_argument("--input", required=True, help="Path to JSON input fixture/payload.")
    parser.add_argument("--output-dir", required=True, help="Directory for generated PDF.")
    args = parser.parse_args()

    report_input = load_report_input(args.input)
    output_path = generate_report_v11(report_input, args.output_dir)
    print(output_path)


if __name__ == "__main__":
    main()
