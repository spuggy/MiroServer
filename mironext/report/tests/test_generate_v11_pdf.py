from miro_v11.models import (
    MiroProject,
    MiroResponseFixture,
    Survey,
    SurveyResponse,
    User,
    init_questions,
)

SURVEY_ID_MIRO_V11 = 11
QUESTION_TRAIL = "33~34~35~36~37~38~39~40~41~42~43~44~45~46~47~48~49~50~51~52~53~54~55~56~57~58~59~60~61~62~63~64~65~66~67~68~69~70~71~72~73~74~75~76~77~78~79~80~81~82~83"
ANSWER_TRAIL = (
    "Tolerant#O;Thorough#A~Understanding#O;Agreeable#A~Empathic#O;Self-starter#D ~"
    "Charismatic#E;Exacting#A~Positive#E;Pioneering#D~Loyal#A;Sceptical#D~"
    "Persuasive#E;Careful#A~Adventurous#D;Temperate#O~Playful#E;Demanding#D~"
    "Admirable#E;Precise#A~Companionable#E;Patient#O~Empathic#O;Meticulous#A~"
    "Unconventional#E;Conventional#A~Level-headed#A;Gregarious#E~Open #E;No-nonsense#D~"
    "Friendly#E;Accurate#A~Stubborn#D;Well-disciplined#A~Relaxed#O;Exacting#A~"
    "Modest#A;Influencing#E~Sophisticated#A;Good-mixer#E~Faithful#A;Popular#E~"
    "Optimistic#E;Analytical#A~Open-minded#A;Self-confident#D~Respectful  #O;Particular#A~"
    "Stable#O;Unpredictable #E~Self-reliant#D;Alert#O~Realistic#D;Sociable#E~"
    "Tolerant#A;Determined#D~Peaceable#A;Convincing#E~Detached#A ;Perfectionist#A ~"
    "null;N/A~null;false#minus~null;false#minus~null;true#minus~null;false#plus~"
    "null;false#minus~null;true#minus~null;false#plus~null;true#minus~null;false#plus~"
    "null;false#minus~null;true#minus~null;true#plus~null;true#minus~null;false#minus~"
    "null;true#minus~null;true#minus~null;true#minus~null;false#minus~null;true#minus"
)


def init_test_data() -> MiroResponseFixture:
    survey_response = SurveyResponse(
        id=1,
        survey_id=SURVEY_ID_MIRO_V11,
        answer_trail=ANSWER_TRAIL,
        question_trail=QUESTION_TRAIL,
    )

    candidate = User(id=1000, first_name="Roger", last_name="Test")
    survey_response.user = candidate

    survey = Survey(id=SURVEY_ID_MIRO_V11, first_question_id=33)
    init_questions(survey)

    practitioner = User(id=2000, first_name="Test", last_name="Practitioner")

    candidate.response_id = survey_response.id
    miro_project = MiroProject()

    return MiroResponseFixture(
        survey=survey,
        survey_response=survey_response,
        candidate=candidate,
        practitioner=practitioner,
        miro_project=miro_project,
    )


def test_generate_sample_v11_pdf() -> None:
    fixture = init_test_data()

    assert fixture.candidate.first_name == "Roger"
    assert fixture.candidate.last_name == "Test"
    assert fixture.candidate.response_id == fixture.survey_response.id
    assert fixture.survey_response.question_trail.startswith("33~34~35")
    assert len(fixture.survey.questions) == 81
    assert fixture.survey.questions[0].id == 1
    assert fixture.survey.questions[-1].id == 82
