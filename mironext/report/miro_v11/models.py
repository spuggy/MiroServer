from dataclasses import dataclass, field
from typing import Any


@dataclass
class User:
    id: int
    first_name: str
    last_name: str
    response_id: int | None = None


@dataclass
class MiroProject:
    metadata: dict[str, Any] = field(default_factory=dict)


@dataclass
class SurveyResponse:
    id: int
    survey_id: int
    answer_trail: str
    question_trail: str
    user: User | None = None


@dataclass(frozen=True)
class Question:
    id: int
    shortname: str
    q_meta: str
    j_question_id: int


@dataclass
class Survey:
    id: int
    first_question_id: int
    questions: list[Question] = field(default_factory=list)


@dataclass
class MiroResponseFixture:
    survey: Survey
    survey_response: SurveyResponse
    candidate: User
    practitioner: User
    miro_project: MiroProject


@dataclass(frozen=True)
class MiroV11ReportInput:
    report_name: str
    candidate_first_name: str
    candidate_last_name: str
    test_id: int
    report_title: str
    page_count: int = 17
    is_free_report: bool = False
    legacy_xhtml_path: str | None = None

    @property
    def full_name(self) -> str:
        return f"{self.candidate_first_name} {self.candidate_last_name}".strip()


V11_QUESTION_DATA: tuple[tuple[int, str, str, int], ...] = (
    (1, "11", "Q", 2),
    (2, "10", "Q", 3),
    (3, "11", "Q", 4),
    (5, "2", "Q", 6),
    (6, "11", "Q", 7),
    (7, "6", "Q", 8),
    (10, "11", "Q", 11),
    (11, "9", "Q", 12),
    (13, "11", "Q", 14),
    (14, "11", "Q", 15),
    (15, "11", "Q", 16),
    (16, "11", "Q", 17),
    (17, "11", "Q", 18),
    (18, "5", "Q", 19),
    (19, "1", "Q", 20),
    (20, "11", "Q", 21),
    (21, "11", "Q", 22),
    (22, "11", "Q", 23),
    (23, "11", "Q", 24),
    (24, "7", "Q", 25),
    (26, "11", "Q", 27),
    (27, "11", "Q", 28),
    (28, "4", "Q", 29),
    (29, "11", "Q", 30),
    (31, "11", "Q", 0),
    (8, "D-O", "tie", 9),
    (4, "E-A", "tie", 5),
    (9, "E-D", "tie", 10),
    (12, "O-A", "tie", 13),
    (25, "E-O", "tie", 26),
    (30, "D-A", "tie", 0),
    (32, "123", "23", 0),
    (33, "11", "Q", 34),
    (34, "6", "Q", 35),
    (35, "D-O", "tie", 36),
    (36, "E-A", "tie", 37),
    (37, "11", "Q", 38),
    (38, "2", "Q", 39),
    (39, "11", "Q", 40),
    (40, "10", "Q", 41),
    (41, "E-D", "tie", 42),
    (42, "11", "Q", 43),
    (43, "9", "Q", 44),
    (44, "O-A", "tie", 45),
    (45, "11", "Q", 46),
    (46, "11", "Q", 47),
    (47, "11", "Q", 48),
    (48, "11", "Q", 49),
    (49, "11", "Q", 50),
    (50, "5", "Q", 51),
    (51, "1", "Q", 52),
    (52, "11", "Q", 53),
    (53, "11", "Q", 54),
    (54, "11", "Q", 55),
    (55, "11", "Q", 56),
    (56, "7", "Q", 57),
    (57, "E-O", "tie", 58),
    (58, "11", "Q", 59),
    (59, "11", "Q", 60),
    (60, "4", "Q", 61),
    (61, "11", "Q", 62),
    (62, "D-A", "tie", 63),
    (63, "", "Q11Inst", 64),
    (64, "", "Q11", 65),
    (65, "", "Q11", 66),
    (66, "", "Q11", 67),
    (67, "", "Q11", 68),
    (68, "", "Q11", 69),
    (69, "", "Q11", 70),
    (70, "", "Q11", 71),
    (71, "", "Q11", 72),
    (72, "", "Q11", 73),
    (73, "", "Q11", 74),
    (74, "", "Q11", 75),
    (75, "", "Q11", 76),
    (76, "", "Q11", 77),
    (77, "", "Q11", 78),
    (78, "", "Q11", 79),
    (79, "", "Q11", 80),
    (80, "", "Q11", 81),
    (81, "", "Q11", 82),
    (82, "", "Q11", 0),
)


def init_questions(survey: Survey) -> None:
    """Populate survey questions with V11 metadata ordering from Java tests."""
    survey.questions = [
        Question(
            id=question_id,
            shortname=shortname,
            q_meta=q_meta,
            j_question_id=j_question_id,
        )
        for question_id, shortname, q_meta, j_question_id in V11_QUESTION_DATA
    ]
