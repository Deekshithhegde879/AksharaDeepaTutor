package com.example.aksharadeepatutor

import android.content.Context
import androidx.compose.ui.platform.LocalContext
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
import com.example.aksharadeepatutor.ui.theme.AksharaDeepaTutorTheme
import androidx.compose.foundation.clickable
import android.content.Intent
import android.net.Uri
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp


data class StudentProgress(
    val xp: Int = 0,
    val level: Int = 3,
    val streak: Int = 5
)
val globalStudentProgress = mutableStateOf(
    StudentProgress()
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AksharaDeepaTutorTheme {
                AksharaDeepaApp()
            }
        }
    }
}

@Composable
fun AksharaDeepaApp() {
    var selectedBottomIndex by remember { mutableStateOf(0) }

    Scaffold(
        bottomBar = {
            NavigationBar {
                val items = listOf("Hub", "Course", "Quiz", "Map", "Profile")
                items.forEachIndexed { index, title ->
                    NavigationBarItem(
                        selected = selectedBottomIndex == index,
                        onClick = { selectedBottomIndex = index },
                        label = { Text(title) },
                        icon = { Text(getIcon(title)) }
                    )
                }
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            when (selectedBottomIndex) {
                0 -> HubScreen(
                    onNavigate = { selectedBottomIndex = it }
                )
                1 -> CourseScreen()
                2 -> QuizScreen()
                3 -> MapScreen()
                4 -> ProfileScreen(globalStudentProgress.value)
            }
        }
    }
}

fun getIcon(title: String): String {
    return when (title) {
        "Hub" -> "⌂"
        "Course" -> "📘"
        "Quiz" -> "🧠"
        "Map" -> "📊"
        "Profile" -> "👤"
        else -> "•"
    }
}

@Composable
fun HubScreen(
    onNavigate: (Int) -> Unit
) {
    val context = LocalContext.current
    val prefs = context.getSharedPreferences("chapters", Context.MODE_PRIVATE)

    val todayChapter = "Chemical Reactions and Equations"
    val todayCompleted = prefs.getBoolean(todayChapter, false)
    val todayProgress = if (todayCompleted) 1f else 0f

    val scienceProgress = getSubjectProgress(
        prefs,
        listOf(
            "Chemical Reactions and Equations",
            "Acids, Bases and Salts",
            "Metals and Non-metals",
            "Life Processes",
            "Light - Reflection and Refraction"
        )
    )

    val mathProgress = getSubjectProgress(
        prefs,
        listOf(
            "Arithmetic Progressions",
            "Triangles",
            "Pair of Linear Equations",
            "Quadratic Equations",
            "Introduction to Trigonometry"
        )
    )

    val socialProgress = getSubjectProgress(
        prefs,
        listOf(
            "Advent of Europeans to India",
            "Extension of British Rule",
            "Our Constitution",
            "Freedom Movement",
            "India After Independence"
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(Color(0xFFF8F6FF))
            .padding(18.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Hello, Deekshith! 👋",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF111827)
                )

                Text(
                    text = "Offline study companion • Grade 10",
                    fontSize = 15.sp,
                    color = Color(0xFF5B6475)
                )
            }

            Text(
                text = "🎓",
                fontSize = 42.sp,
                modifier = Modifier
                    .background(Color(0xFFEDE4FF), RoundedCornerShape(50.dp))
                    .padding(14.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.cardElevation(8.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF3EAFE))
        ) {
            Column(modifier = Modifier.padding(22.dp)) {

                Text(
                    text = if (todayCompleted) "✅ CONGRATS!" else "⭐ TODAY'S GOAL",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF4338CA),
                    modifier = Modifier
                        .background(Color(0xFFE5D8FF), RoundedCornerShape(30.dp))
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                )

                Spacer(modifier = Modifier.height(18.dp))

                Text(
                    text = if (todayCompleted)
                        "You completed today's goal!"
                    else
                        "Complete 1 Chapter of Science:",
                    fontSize = 17.sp,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Chemical Reactions and Equations",
                    fontSize = 21.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF4338CA)
                )

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (todayCompleted) "100%" else "0%",
                        color = Color(0xFF4338CA),
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    LinearProgressIndicator(
                        progress = { todayProgress },
                        modifier = Modifier.weight(1f)
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Text(if (todayCompleted) "1 / 1 Chapter" else "0 / 1 Chapter")
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.cardElevation(8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(modifier = Modifier.padding(22.dp)) {

                Text(
                    text = "📊 Subject Progress",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(20.dp))

                ModernSubjectProgress("🧪", "Science", scienceProgress)
                ModernSubjectProgress("√x", "Math", mathProgress)
                ModernSubjectProgress("🌍", "Social Studies", socialProgress)

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "View detailed progress  ›",
                    color = Color(0xFF4338CA),
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.Transparent)
        ) {
            Column(
                modifier = Modifier
                    .background(
                        Brush.horizontalGradient(
                            listOf(
                                Color(0xFF4338CA),
                                Color(0xFF5B4BFF)
                            )
                        )
                    )
                    .padding(22.dp)
            ) {
                Text(
                    text = "🤖  STUDY SAGE AI ✨",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 19.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Based on your recent quiz history, you should focus more on Science. Completing the 'Chemical Reactions and Equations' chapter will strengthen your map.",
                    color = Color.White,
                    fontSize = 15.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(22.dp))

        Text(
            text = "Quick Actions",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(14.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            QuickActionCard("📘", "Continue\nLearning") {
                onNavigate(1)
            }

            QuickActionCard("✅", "Take a\nQuiz") {
                onNavigate(2)
            }

            QuickActionCard("📝", "My\nNotes") {
                onNavigate(1)
            }

            QuickActionCard("▶️", "Watch\nVideos") {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://www.youtube.com/results?search_query=class+10+science")
                )
                context.startActivity(intent)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
fun CourseScreen() {

    val context = LocalContext.current

    val prefs = context.getSharedPreferences(
        "chapters",
        Context.MODE_PRIVATE
    )

    val scienceChapters = remember {
        mutableStateListOf(
            "Chemical Reactions and Equations",
            "Acids, Bases and Salts",
            "Metals and Non-metals",
            "Life Processes",
            "Light - Reflection and Refraction"
        )
    }

    val mathChapters = remember {
        mutableStateListOf(
            "Arithmetic Progressions",
            "Triangles",
            "Pair of Linear Equations",
            "Quadratic Equations",
            "Introduction to Trigonometry"
        )
    }

    val socialChapters = remember {
        mutableStateListOf(
            "Advent of Europeans to India",
            "Extension of British Rule",
            "Our Constitution",
            "Freedom Movement",
            "India After Independence"
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {

        Text(
            text = "Chapter Tracker",
            style = MaterialTheme.typography.headlineMedium
        )

        Text("Tick off your completed syllabus.")

        Spacer(modifier = Modifier.height(20.dp))

        PersistentChapterSection(
            subject = "SCIENCE",
            chapters = scienceChapters,
            prefs = prefs
        )

        Spacer(modifier = Modifier.height(20.dp))

        PersistentChapterSection(
            subject = "MATH",
            chapters = mathChapters,
            prefs = prefs
        )

        Spacer(modifier = Modifier.height(20.dp))

        PersistentChapterSection(
            subject = "SOCIAL STUDIES",
            chapters = socialChapters,
            prefs = prefs
        )
    }
}

@Composable
fun MapScreen() {
    val context = LocalContext.current
    val prefs = context.getSharedPreferences("chapters", Context.MODE_PRIVATE)

    val scienceChapters = listOf(
        "Chemical Reactions and Equations",
        "Acids, Bases and Salts",
        "Metals and Non-metals",
        "Life Processes",
        "Light - Reflection and Refraction"
    )

    val mathChapters = listOf(
        "Arithmetic Progressions",
        "Triangles",
        "Pair of Linear Equations",
        "Quadratic Equations",
        "Introduction to Trigonometry"
    )

    val socialChapters = listOf(
        "Advent of Europeans to India",
        "Extension of British Rule",
        "Our Constitution",
        "Freedom Movement",
        "India After Independence"
    )

    val scienceChapterProgress = getSubjectProgress(prefs, scienceChapters)
    val mathChapterProgress = getSubjectProgress(prefs, mathChapters)
    val socialChapterProgress = getSubjectProgress(prefs, socialChapters)

    val scienceQuizProgress = getQuizProgress(prefs, "Science")
    val mathQuizProgress = getQuizProgress(prefs, "Math")
    val socialQuizProgress = getQuizProgress(prefs, "Social Studies")

    val scienceMastery = calculateMastery(scienceChapterProgress, scienceQuizProgress)
    val mathMastery = calculateMastery(mathChapterProgress, mathQuizProgress)
    val socialMastery = calculateMastery(socialChapterProgress, socialQuizProgress)

    val weakestSubject = getWeakestSubject(
        scienceMastery,
        mathMastery,
        socialMastery
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {

        Text("Strength Map", style = MaterialTheme.typography.headlineMedium)
        Text("Based on chapters completed and quiz performance.")

        Spacer(modifier = Modifier.height(20.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(6.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {

                Text("Overall Mastery", style = MaterialTheme.typography.titleLarge)

                Spacer(modifier = Modifier.height(16.dp))

                SubjectProgressItem("Science", scienceMastery)
                Spacer(modifier = Modifier.height(14.dp))

                SubjectProgressItem("Math", mathMastery)
                Spacer(modifier = Modifier.height(14.dp))

                SubjectProgressItem("Social Studies", socialMastery)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(6.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {

                Text("Subject Breakdown", style = MaterialTheme.typography.titleLarge)

                Spacer(modifier = Modifier.height(16.dp))

                BreakdownItem("Science", "${(scienceMastery * 100).toInt()}/100")
                BreakdownItem("Math", "${(mathMastery * 100).toInt()}/100")
                BreakdownItem("Social Studies", "${(socialMastery * 100).toInt()}/100")
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Column(modifier = Modifier.padding(20.dp)) {

                Text(
                    text = "STUDY SAGE AI",
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "$weakestSubject is currently your weakest area. Revise pending chapters and attempt more quizzes to improve your mastery score.",
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}

@Composable
fun ProfileScreen(
    progress: StudentProgress
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {

        Text(
            text = "Student Profile",
            style = MaterialTheme.typography.headlineMedium
        )

        Text("Track your learning journey.")

        Spacer(modifier = Modifier.height(20.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(6.dp)
        ) {

            Column(
                modifier = Modifier.padding(20.dp)
            ) {

                Text(
                    text = "Deekshith",
                    style = MaterialTheme.typography.headlineSmall
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text("Grade 10 Student")
                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Level ${progress.level}",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(10.dp))

                LinearProgressIndicator(
                    progress = { (progress.xp % 200) / 200f },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text("${progress.xp % 200} XP / 200 XP")

                Spacer(modifier = Modifier.height(20.dp))

                ProfileStatItem("Completed Chapters", "1")
                ProfileStatItem("Quizzes Attempted", "12")
                ProfileStatItem("Average Score", "74%")
                ProfileStatItem("Current Streak", "5 Days")
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(6.dp)
        ) {

            Column(
                modifier = Modifier.padding(20.dp)
            ) {

                Text(
                    text = "Achievements",
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(modifier = Modifier.height(16.dp))

                AchievementItem("🏆 Quiz Master")
                AchievementItem("🔥 5 Day Streak")
                AchievementItem("📘 Science Explorer")
                AchievementItem("⭐ Fast Learner")
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {

            Column(
                modifier = Modifier.padding(20.dp)
            ) {

                Text(
                    text = "Learning Insight",
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "You are improving consistently. Your Science performance has increased by 12% this week.",
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}

@Composable
fun SubjectProgressItem(
    subject: String,
    progress: Float
) {

    Column {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Text(text = subject)

            Text(text = "${(progress * 100).toInt()}%")
        }

        Spacer(modifier = Modifier.height(6.dp))

        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun PersistentChapterSection(
    subject: String,
    chapters: List<String>,
    prefs: android.content.SharedPreferences
) {

    val checkedStates = remember {

        chapters.map {

            mutableStateOf(
                prefs.getBoolean(it, false)
            )
        }
    }

    val completedCount = checkedStates.count { it.value }
    var selectedChapter by remember { mutableStateOf<String?>(null) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Text(
                    text = subject,
                    style = MaterialTheme.typography.titleLarge
                )

                Text(
                    text = "$completedCount/${chapters.size} DONE"
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            chapters.forEachIndexed { index, chapter ->

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            selectedChapter = chapter
                        }
                        .padding(vertical = 8.dp)
                ) {

                    Checkbox(
                        checked = checkedStates[index].value,
                        onCheckedChange = { checked ->

                            checkedStates[index].value = checked

                            prefs.edit()
                                .putBoolean(chapter, checked)
                                .apply()

                            if (checked) {
                                val current = globalStudentProgress.value
                                val newXp = current.xp + 20

                                globalStudentProgress.value = current.copy(
                                    xp = newXp,
                                    level = (newXp / 200) + 1
                                )
                            } else {
                                val current = globalStudentProgress.value
                                val newXp = (current.xp - 20).coerceAtLeast(0)

                                globalStudentProgress.value = current.copy(
                                    xp = newXp,
                                    level = (newXp / 200) + 1
                                )
                            }
                        }
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = chapter,
                        modifier = Modifier.padding(top = 12.dp)
                    )
                }


                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = chapter,
                        modifier = Modifier.padding(top = 12.dp)
                    )
                }
            selectedChapter?.let { chapter ->
                NotesDialog(
                    chapterName = chapter,
                    onClose = { selectedChapter = null }
                )
            }
            }
        }
    }


@Composable
fun QuizCard(
    title: String,
    onStart: () -> Unit
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {

        Column(
            modifier = Modifier.padding(20.dp)
        ) {

            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "5 Questions • 2.5 Minutes"
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onStart,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Start Quiz")
            }
        }
    }
}

data class QuizQuestion(
    val question: String,
    val options: List<String>,
    val correctAnswer: String
)

@Composable
fun QuizScreen() {
    var quizStarted by remember { mutableStateOf(false) }
    var selectedSubject by remember { mutableStateOf("Science") }

    if (quizStarted) {
        QuizQuestionScreen(selectedSubject)
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text("Quiz Mode", style = MaterialTheme.typography.headlineMedium)
            Text("Active learning through testing.")

            Spacer(modifier = Modifier.height(20.dp))

            QuizCard("Science Mock Quiz") {
                selectedSubject = "Science"
                quizStarted = true
            }

            Spacer(modifier = Modifier.height(16.dp))

            QuizCard("Math Mock Quiz") {
                selectedSubject = "Math"
                quizStarted = true
            }

            Spacer(modifier = Modifier.height(16.dp))

            QuizCard("Social Studies Mock Quiz") {
                selectedSubject = "Social Studies"
                quizStarted = true
            }
        }
    }
}

@Composable
fun QuizQuestionScreen(subject: String) {

    val questions = listOf(

        QuizQuestion(
            question = "Which gas is released when acid reacts with metal?",
            options = listOf(
                "Oxygen",
                "Hydrogen",
                "Nitrogen",
                "Carbon dioxide"
            ),
            correctAnswer = "Hydrogen"
        ),

        QuizQuestion(
            question = "What is the SI unit of force?",
            options = listOf(
                "Joule",
                "Newton",
                "Pascal",
                "Watt"
            ),
            correctAnswer = "Newton"
        ),

        QuizQuestion(
            question = "Which planet is called the Red Planet?",
            options = listOf(
                "Earth",
                "Venus",
                "Mars",
                "Jupiter"
            ),
            correctAnswer = "Mars"
        ),

        QuizQuestion(
            question = "Who wrote the Constitution of India?",
            options = listOf(
                "Mahatma Gandhi",
                "Dr. B.R. Ambedkar",
                "Jawaharlal Nehru",
                "Subhash Chandra Bose"
            ),
            correctAnswer = "Dr. B.R. Ambedkar"
        ),

        QuizQuestion(
            question = "What is √64 ?",
            options = listOf(
                "6",
                "7",
                "8",
                "9"
            ),
            correctAnswer = "8"
        )
    )

    var currentQuestionIndex by remember {
        mutableStateOf(0)
    }

    var score by remember {
        mutableStateOf(0)
    }

    var selectedAnswer by remember {
        mutableStateOf("")
    }

    var quizFinished by remember {
        mutableStateOf(false)
    }

    if (quizFinished) {

        ResultScreen(
            score = score,
            total = questions.size,
            subject = subject
        )

    } else {

        val currentQuestion = questions[currentQuestionIndex]

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            Text(
                text = "Question ${currentQuestionIndex + 1}/${questions.size}",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(20.dp))

            Card(
                modifier = Modifier.fillMaxWidth()
            ) {

                Column(
                    modifier = Modifier.padding(20.dp)
                ) {

                    Text(
                        text = currentQuestion.question,
                        style = MaterialTheme.typography.titleLarge
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    currentQuestion.options.forEach { option ->

                        Button(
                            onClick = {
                                selectedAnswer = option
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {

                            Text(option)
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = {

                            if (selectedAnswer == currentQuestion.correctAnswer) {
                                score++
                            }

                            if (currentQuestionIndex < questions.size - 1) {

                                currentQuestionIndex++
                                selectedAnswer = ""

                            } else {

                                quizFinished = true
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        Text(
                            if (currentQuestionIndex == questions.size - 1)
                                "Submit Quiz"
                            else
                                "Next Question"
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ResultScreen(
    score: Int,
    total: Int,
    subject: String
) {
    val context = LocalContext.current
    val prefs = context.getSharedPreferences("chapters", Context.MODE_PRIVATE)

    LaunchedEffect(Unit) {
        val percentage = score.toFloat() / total.toFloat()

        prefs.edit()
            .putFloat("${subject}_quiz_score", percentage)
            .apply()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "Quiz Completed!",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Your Score",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "$score / $total",
            style = MaterialTheme.typography.displayMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(30.dp))

        if (score >= 4) {

            Text("Excellent Performance!")

        } else if (score >= 2) {

            Text("Good Job! Keep Practicing.")

        } else {

            Text("You should revise this subject more.")
        }
    }
}

@Composable
fun BreakdownItem(
    subject: String,
    score: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(subject)
        Text(
            text = score,
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Composable
fun ProfileStatItem(
    title: String,
    value: String
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Text(title)

        Text(
            text = value,
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Composable
fun AchievementItem(
    achievement: String
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {

        Text(
            text = achievement,
            modifier = Modifier.padding(16.dp)
        )
    }
}
@Composable
fun NotesDialog(
    chapterName: String,
    onClose: () -> Unit
) {

    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = onClose,

        title = {
            Text(chapterName)
        },

        text = {

            Column {

                Text(
                    text = getChapterNotes(chapterName)
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {

                        val intent = Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse(getYoutubeLink(chapterName))
                        )

                        context.startActivity(intent)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Text("Watch Explanation Video")
                }
            }
        },

        confirmButton = {

            Button(
                onClick = onClose
            ) {

                Text("Close")
            }
        }
    )
}

fun getChapterNotes(chapterName: String): String {
    return when (chapterName) {

        "Chemical Reactions and Equations" ->
            "This chapter explains chemical reactions, balanced equations, combination reactions, decomposition reactions, displacement reactions, oxidation and reduction. Important point: a chemical equation must be balanced because mass is conserved."

        "Acids, Bases and Salts" ->
            "This chapter covers properties of acids and bases, indicators, pH scale, neutralization reactions, salts, baking soda, washing soda, plaster of Paris, and their daily-life applications."

        "Metals and Non-metals" ->
            "This chapter explains physical and chemical properties of metals and non-metals, reactivity series, ionic compounds, corrosion, and methods to prevent corrosion."

        "Life Processes" ->
            "This chapter covers nutrition, respiration, transportation, and excretion in living organisms. It explains how plants and animals obtain energy and remove waste."

        "Light - Reflection and Refraction" ->
            "This chapter explains reflection, refraction, mirrors, lenses, image formation, focal length, magnification, and important ray diagrams."

        "Arithmetic Progressions" ->
            "This chapter explains number patterns where the difference between consecutive terms is constant. Important formulas: nth term = a + (n-1)d and sum = n/2[2a + (n-1)d]."

        "Triangles" ->
            "This chapter covers similarity of triangles, criteria such as AAA, SAS, SSS, Pythagoras theorem, and related geometry problems."

        "Pair of Linear Equations" ->
            "This chapter explains solving two linear equations using graphical method, substitution method, elimination method, and cross multiplication method."

        "Quadratic Equations" ->
            "This chapter covers quadratic equations, factorization, completing the square, quadratic formula, and nature of roots using discriminant."

        "Introduction to Trigonometry" ->
            "This chapter explains trigonometric ratios sin, cos, tan, cosec, sec, cot, standard angle values, and basic identities."

        "Advent of Europeans to India" ->
            "This chapter explains the arrival of Portuguese, Dutch, French, and British traders in India and how trade later led to political control."

        "Extension of British Rule" ->
            "This chapter covers how British power expanded in India through wars, policies, alliances, and administrative control."

        "Our Constitution" ->
            "This chapter explains the meaning of Constitution, fundamental rights, duties, directive principles, democracy, and the structure of government."

        "Freedom Movement" ->
            "This chapter covers important movements, leaders, protests, and events that contributed to Indian independence."

        "India After Independence" ->
            "This chapter explains challenges after independence, integration of states, planning, development, democracy, and nation-building."

        else ->
            "Notes for this chapter will be added soon."
    }
}

fun getYoutubeLink(chapterName: String): String {

    return when (chapterName) {

        "Chemical Reactions and Equations" ->
            "https://www.youtube.com/watch?v=E9M3VqkXm6E"

        "Acids, Bases and Salts" ->
            "https://www.youtube.com/watch?v=5t0vTLda0b4"

        "Metals and Non-metals" ->
            "https://www.youtube.com/watch?v=Q5T4sRkQ2gQ"

        "Life Processes" ->
            "https://www.youtube.com/watch?v=4z7P4t4Q5n8"

        "Light - Reflection and Refraction" ->
            "https://www.youtube.com/watch?v=YvL8w2JQfJY"

        "Arithmetic Progressions" ->
            "https://www.youtube.com/watch?v=9MUDAr1PnlE"

        "Triangles" ->
            "https://www.youtube.com/watch?v=QVGXwM8Q6x8"

        "Pair of Linear Equations" ->
            "https://www.youtube.com/watch?v=6t6m4o6z0xY"

        "Quadratic Equations" ->
            "https://www.youtube.com/watch?v=qeByhTF8WEw"

        "Introduction to Trigonometry" ->
            "https://www.youtube.com/watch?v=PUB0TaZ7bhA"

        "Advent of Europeans to India" ->
            "https://www.youtube.com/watch?v=jv7D5H8W6iA"

        "Extension of British Rule" ->
            "https://www.youtube.com/watch?v=4jL8R0x3Q4Y"

        "Our Constitution" ->
            "https://www.youtube.com/watch?v=6m4x8xY8T5Q"

        "Freedom Movement" ->
            "https://www.youtube.com/watch?v=Q4B6QxvP5J8"

        "India After Independence" ->
            "https://www.youtube.com/watch?v=wA0m8Lx0X3Q"

        else ->
            "https://www.youtube.com"
    }
}

@Composable
fun ModernSubjectProgress(
    icon: String,
    subject: String,
    progress: Float
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = icon,
            fontSize = 24.sp,
            modifier = Modifier
                .background(Color(0xFFEDE4FF), RoundedCornerShape(14.dp))
                .padding(12.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(subject, fontSize = 16.sp)
                Text("${(progress * 100).toInt()}%")
            }

            Spacer(modifier = Modifier.height(6.dp))

            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun QuickActionCard(
    icon: String,
    title: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(72.dp)
            .height(105.dp)
            .clickable {
                onClick()
            },
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(icon, fontSize = 26.sp)

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = title,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun getSubjectProgress(
    prefs: android.content.SharedPreferences,
    chapters: List<String>
): Float {
    val completed = chapters.count { chapter ->
        prefs.getBoolean(chapter, false)
    }

    return completed.toFloat() / chapters.size.toFloat()
}

fun getQuizProgress(
    prefs: android.content.SharedPreferences,
    subject: String
): Float {
    return prefs.getFloat("${subject}_quiz_score", 0f)
}

fun calculateMastery(
    chapterProgress: Float,
    quizProgress: Float
): Float {
    return (chapterProgress * 0.6f) + (quizProgress * 0.4f)
}

fun getWeakestSubject(
    science: Float,
    math: Float,
    social: Float
): String {
    return when {
        science <= math && science <= social -> "Science"
        math <= science && math <= social -> "Math"
        else -> "Social Studies"
    }
}
