# MiRo V11 Python Report

Python ReportLab-based V11 report generator scaffold.

## Layout

- `miro_v11/`: generator package
- `fixtures/`: sample input payloads
- `tests/`: pytest suite
- `out/`: generated local PDFs

## Setup

From `mironext/`:

```bash
python3 -m venv report/.venv
report/.venv/bin/pip install -r report/requirements.txt
```

## Generate Sample PDF

From `mironext/`:

```bash
PYTHONPATH=report report/.venv/bin/python -m miro_v11.cli --input report/fixtures/sample_v11.json --output-dir report/out
```

Current status: the Python `MiroReport` and `MiroReportFileGenerator` are scaffolds only. The generation entrypoint is wired, but report rendering raises `NotImplementedError` until the full port is completed.

## Run Tests

From `mironext/`:

```bash
PYTHONPATH=report report/.venv/bin/python -m pytest report/tests -q
```
