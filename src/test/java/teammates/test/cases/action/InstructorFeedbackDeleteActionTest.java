package teammates.test.cases.action;

import org.testng.annotations.Test;

import teammates.common.datatransfer.attributes.FeedbackSessionAttributes;
import teammates.common.datatransfer.attributes.InstructorAttributes;
import teammates.common.util.Const;
import teammates.storage.api.FeedbackSessionsDb;
import teammates.ui.controller.InstructorFeedbackDeleteAction;
import teammates.ui.controller.RedirectResult;

public class InstructorFeedbackDeleteActionTest extends BaseActionTest {

    @Override
    protected String getActionUri() {
        return Const.ActionURIs.INSTRUCTOR_FEEDBACK_DELETE;
    }

    @Override
    @Test
    public void testExecuteAndPostProcess() {
        FeedbackSessionsDb fsDb = new FeedbackSessionsDb();
        FeedbackSessionAttributes fs = dataBundle.feedbackSessions.get("session1InCourse1");

        String[] submissionParams = new String[]{
                Const.ParamsNames.COURSE_ID, fs.getCourseId(),
                Const.ParamsNames.FEEDBACK_SESSION_NAME, fs.getFeedbackSessionName(),
        };

        InstructorAttributes instructor = dataBundle.instructors.get("instructor1OfCourse1");
        gaeSimulation.loginAsInstructor(instructor.googleId);

        assertNotNull(fsDb.getFeedbackSession(fs.getCourseId(), fs.getFeedbackSessionName()));

        InstructorFeedbackDeleteAction a = getAction(submissionParams);
        RedirectResult r = getRedirectResult(a);

        assertNull(fsDb.getFeedbackSession(fs.getCourseId(), fs.getFeedbackSessionName()));
        assertEquals(Const.ActionURIs.INSTRUCTOR_FEEDBACKS_PAGE
                         + "?error=false&user=idOfInstructor1OfCourse1",
                     r.getDestinationWithParams());
        assertEquals(Const.StatusMessages.FEEDBACK_SESSION_DELETED, r.getStatusMessage());
        assertFalse(r.isError);
    }

    @Override
    protected InstructorFeedbackDeleteAction getAction(String... params) {
        return (InstructorFeedbackDeleteAction) gaeSimulation.getActionObject(getActionUri(), params);
    }
}
