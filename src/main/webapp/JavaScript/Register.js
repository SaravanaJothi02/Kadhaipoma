
  const steps = document.querySelectorAll(".form-step");
  const nextButton = document.getElementById("nextButton");
  const prevButton = document.getElementById("prevButton");
  const submitButton = document.getElementById("submitButton");
  let currentStep = 0;
  let isValid = true;

  function showStep(index) {
    steps.forEach((step, idx) => {
      step.classList.toggle("active", idx === index);
    });
    // 
    if (currentStep === 0) {
      prevButton.classList.remove("active");
  } else {
      prevButton.classList.add("active");
  }
    nextButton.style.display = index < steps.length - 1 ? "inline-block" : "none";
    submitButton.style.display = index === steps.length - 1 ? "inline-block" : "none";
  }

  function checkDate(dob) {
    const inputDate = new Date(dob);
    const today = new Date();
    inputDate.setHours(0, 0, 0, 0);
    today.setHours(0, 0, 0, 0);
  
    if (inputDate > today) {
      return false;
    } else {
      return true;
    }
  }
  

  function validateCurrentStep() {

    if (currentStep === 0) {
      // Validate Personal Details (Step 1)
      const name = document.getElementById("name").value.trim();
      const dob = document.getElementById("dob").value.trim();
      const gender = document.getElementById("gender").value.trim();

      if (!name) {
        alert("Please enter your name.");
        isValid = false;
      } else if (!dob) {
        alert("Please select your date of birth.");
        isValid = false;
      }
      else if ( ! checkDate(dob)){
        alert("Please Select the correct date");
        isValid=false;
      }
       else if (!gender) {
        alert("Please select your gender.");
        isValid = false;
      }
    } else if (currentStep === 1) {
      // Validate Account Details (Step 2)
      const username = document.getElementById("username").value.trim();
      const email = document.getElementById("email").value.trim();
      const password = document.getElementById("password").value.trim();
      const confirmPassword = document.getElementById("confirmPassword").value.trim();

      let passwordPattern = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[!@#$%^&*])[A-Za-z\d!@#$%^&*]{8,}$/;

      if (!username) {
        alert("Please enter a username.");
        isValid = false;
      } else if (!email || !/\S+@\S+\.\S+/.test(email)) {
        alert("Please enter a valid email address.");
        isValid = false;
      }
    //  else if (!password) {
    //     alert("Please enter a password.");
    //     isValid = false;
    //   } 
      else if(! passwordPattern.test(password)) {
        alert("Password must be at least 8 characters long, contain at least one letter, one number, and one special character.");
        isValid= false;   
      }
      else if (!confirmPassword) {
        alert("Please confirm your password.");
        isValid = false;
      } else if (password !== confirmPassword) {
        alert("Passwords do not match.");
        isValid = false;
      }
    }

    return isValid;
  }

  function nextStep() {
    if (validateCurrentStep() && currentStep < steps.length - 1) {
      currentStep++;
      showStep(currentStep);
    }
  }

  function prevStep() {
    if (currentStep > 0) {
      currentStep--;
      showStep(currentStep);
    }
  }

  // Initial setup
  showStep(currentStep);

  // Form submission
  document.getElementById("multiStepForm").addEventListener("submit", (e) => {
    e.preventDefault();
    if(isValid)
    alert("Form submitted successfully!");
  });
