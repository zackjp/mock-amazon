
# Mock Amazon App

## Purpose
A recreation of the Amazon shopping app in Compose to research and validate latest patterns, libraries, and tools. This includes modularization, MVI frameworks, performance testing, analytics, and AI-assisted workflows.

## Screenshots
<img src="screenshots/mock01_home_screen.png" height="400"/> <img src="screenshots/mock02_search_suggestions.png" height="400"/> <img src="screenshots/mock03_search_results.png" height="400"/>

<img src="screenshots/mock04_cart.png" height="400"/> <img src="screenshots/mock05a_product.png" height="400"/> <img src="screenshots/mock05b_product_scrolled.png" height="400"/>


## Setup

### Firebase Analytics
Copy the `google-services.json.template` to `app/google-services.json` file and insert your Firebase credentials.

### Claude CLI

Open the Android Studio terminal and start the Docker container:

```bash
cd claude
docker compose up -d --build
```

Then open an interactive shell via:

```bash
docker exec -it mockamazon-android-claude-cli-1 bash
```

The project root is mounted at `/workspace` inside the container. From there, run `claude` to start a session.
