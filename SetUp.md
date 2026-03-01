# Example:

# Classroom: OOPS + DSA Batch 2026
    ├── Assignment 1: Intro to OOPS
    ├── Assignment 2: Encapsulation Practice
    ├── Assignment 3: Inheritance & Polymorphism
    ├── Assignment 4: Arrays
    ├── Assignment 5: Strings

# 🚀 GitHub Classroom + Codespaces Setup Guide  
(Trainer + Student Instructions)

This document explains how to use GitHub Classroom with Codespaces for Java OOPS/DSA training.

---

# 📘 FOR TRAINERS

## ✅ Do You Need to Create a Classroom Every Time?

No.

You create:

• ONE Classroom per batch/course  
• ONE Assignment per topic  

Example structure:

Classroom: OOPS + DSA Batch 2026  
  - Assignment 1: Intro to OOPS  
  - Assignment 2: Encapsulation Practice  
  - Assignment 3: Inheritance  
  - Assignment 4: Arrays  
  - Assignment 5: Strings  

You DO NOT create a new classroom for each topic.

---

## 🏗 Step 1 — Create a Classroom (One-Time Setup)

1. Go to: https://classroom.github.com  
2. Click **New classroom**  
3. Select your organization  
4. Complete setup  

---

## 📝 Step 2 — Create an Assignment (For Each Topic)

1. Open your Classroom  
2. Click **New Assignment**  
3. Choose **Individual Assignment**  
4. Select your starter template repository  
5. Set Repository Visibility to **Public** (recommended for free Codespaces use)  
6. Skip autograding (optional)  
7. Click **Create Assignment**  

---

## 🔗 Step 3 — Share the Invitation Link

After creating the assignment, GitHub generates a link like:

https://classroom.github.com/a/XXXXXXXX

This is the ONLY link you share with students.

Do NOT share:
- Your template repository link
- Your Codespace link

---

## 📊 How Trainers Grade

1. Go to https://classroom.github.com  
2. Open your classroom  
3. Click the assignment  
4. You will see all student repositories  
5. Click each repo to review commits  

Each student has their own isolated repository.

---

# 🎓 FOR STUDENTS

## 🟢 Step 1 — Accept Assignment

1. Click the invitation link shared by trainer  
2. Log into GitHub  
3. Click **Accept Assignment**  
4. GitHub creates your personal assignment repository  

---

## 💻 Step 2 — Open Codespaces

1. Open your new repository  
2. Click **Code**  
3. Click **Codespaces**  
4. Click **Create Codespace**  

Wait for environment to load.

---

## 🔄 Step 3 — Pull Latest Code (If Needed)

Open Terminal:

```
git checkout main
git pull
```

---

## 🛠 Step 4 — Compile Java File

```
javac OOPSSimpleDemo.java
```

---

## ▶ Step 5 — Run Program

```
java OOPSSimpleDemo
```

Important:
- Do NOT type `.java` when running
- Only use class name

---

## 💾 Step 6 — Save & Submit Work

After making changes:

```
git status
git add .
git commit -m "Completed assignment"
git push
```

Your submission is automatically saved.

---

# 🔒 Why This Setup Is Safe

• Each student gets their own repository  
• No one can override another student's code  
• Trainer template repository remains untouched  
• Clean grading workflow  
• Fully isolated environments  

---

# ⚠ Common Errors

Error:  
can't find main(String[]) method  

Make sure:
- You are running the correct class  
- The class contains:

public static void main(String[] args)

---

# 🎯 Training Best Practices

• Keep template repo clean  
• Do not allow students to work directly in template  
• Create one assignment per topic  
• Ask students to stop Codespaces when done  

---

Happy Coding 🚀
