# ⏳ TimeSwap - Time Exchange Platform  

## 📌 Overview  
**TimeSwap (TS)** is an innovative **time exchange platform** designed to connect people who have free time with those who need assistance, primarily in **Service and Information Technology (IT)**. The platform enables users to **offer and receive help** based on their **skills, availability, and expertise**.  

---

## ✨ Features  

### 👤 User Authentication  
✅ **Login / Register** with email & phone number.  
✅ **Forgot Password** and **Change Password** support.  
✅ **Secure Logout** from the system.  

### 📋 Job Management  
✅ **Job Listings**: View all available jobs with essential details.  
✅ **Job Search**: Search jobs by **field, salary, and time**.  
✅ **Apply for Jobs**: Submit applications with user profiles.  
✅ **Job Details**: View full **job descriptions and requirements**.  
✅ **Job Applicant List**: Employers can review applicants and select the most suitable candidate.  

### 🌍 Location Map Integration  
✅ **Google Maps API integration** to display current user location.  
✅ **Job Location Search**: Helps users find nearby job postings.  

### 🤖 AI Chatbot Integration  
✅ **Integrated ChatGPT API** to assist users with job-related inquiries.  

### 💳 Payment Management  
✅ **E-Wallet Integration** for seamless transactions.  
✅ **Transaction History** to keep track of payments.  

### 📄 User Profile Management  
✅ **View & Edit Profile**: Update industry, category, and location.  
✅ **Account Information**: View subscription details and current balance.  
✅ **Credibility System**: Users earn **credibility points** based on **completed jobs, ratings, and evaluations**.  

---

## 🛠️ Technologies Used  

- 🟢 **Kotlin** (Android Development)  
- 🏗️ **Jetpack Compose** (UI Framework)  
- 🔀 **Navigation Component**  
- 🌍 **Google Maps API**  
- 🤖 **ChatGPT API**  
- ⚡ **Retrofit** for API calls  
- 🏢 **MVVM Architecture**  

---

## 🚀 Installation Guide  

### 📌 Prerequisites  
- Install **Android Studio** (Latest Version).  
- Set up an **Android Emulator** or connect a **real Android device**.  

### 🛠️ Steps to Run the Project  
1️⃣ Clone this repository:  
   ```sh
   git clone https://github.com/SangTran13/TimeSwap-Android.git
   ```  
2️⃣ Open the project in **Android Studio**.  
3️⃣ Sync **Gradle** and install dependencies.  
4️⃣ Obtain a **Google Maps API Key** from **Google Cloud Console**.  
5️⃣ Add your API key and other environment variables in **local.properties**:  

   ```properties
   BASE_AUTH_URL=YOUR_LINK_API
   BASE_API_URL=YOUR_LINK_API
   CONFIRM_EMAIL_AUTH_URL=YOUR_LINK_API
   BASE_GPT_URL=https://api.openai.com/v1/
   KEY_GPT_API=YOUR_GPT_API_KEY
   KEY_ORGANIZATION_GPT_API=YOUR_ORGANIZATION_KEY
   GOOGLE_MAPS_API_KEY=YOUR_GOOGLE_MAPS_API_KEY
   ```  
6️⃣ Run the project on an **emulator or real device**.  

---

## 🔒 Non-Functional Requirements  

### 🔐 Security  
✅ **Password Hashing** to protect user credentials.  
✅ **Email & Phone Verification** for user authentication.  
✅ **Sensitive Data Protection** (hiding emails, phone numbers, and financial details).  

### ⚡ Performance  
✅ **Optimized Database Queries** for quick job searches.  
✅ **Lazy Loading** for job listings and profile data.  
✅ **Efficient Image & API Data Caching** to improve speed.  

### 📈 Scalability  
✅ **Supports a large number of users** and job listings.  
✅ **Can be expanded** to include new job categories and AI capabilities.  

### 🔄 Reliability  
✅ **Automated error handling** for a smooth user experience.  
✅ **Minimal downtime** using robust cloud infrastructure.  

---

## 🔗 Important Links  

🔹 **GitHub Repository**: [My repository project](https://github.com/SangTran13/time-swap-android)  
🔹 **APK Download**: [Download APK](https://drive.google.com/file/d/18l0deLnGPnUns_6qkaC98DUORBHSn9WD/view)  

---

## 👨‍💻 Author  

📌 **GROUP 3 - PRM392**  
📧 **Email**: [tranngocsang147877@gmail.com](mailto:tranngocsang147877@gmail.com)  
🔗 **GitHub**: [github.com/sangtran13](https://github.com/sangtran13)  

---

## 📜 License  

This project is licensed under the **MIT License** – see the [LICENSE](./LICENSE) file for details.  

---

🚀 **This README is well-structured for clarity, making it easy for developers and contributors to navigate. Let me know if you need any modifications!** 😊  
