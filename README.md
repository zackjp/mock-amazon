
# Mock Amazon App

## Purpose
A recreation of the Amazon shopping app in Compose to research and validate latest patterns, libraries, and tools. This includes modularization, MVI frameworks, performance testing, analytics, and AI-assisted workflows.

## Screenshots
<img alt="Home screenshot 1" src="screenshots/s01a_home_hero.png" height="400"/> <img alt="Home screenshot 2" src="screenshots/s01b_home_hero.png" height="400"/> <img alt="Home screenshot 3" src="screenshots/s01c_home_hero.png" height="400"/> <img alt="Home screenshot 4" src="screenshots/s01d_home_intent_carousels.png" height="400"/>

<img alt="Search screenshot" src="screenshots/s02_search.png" height="400"/> <img alt="Search results screenshot" src="screenshots/s03_search_results.png" height="400"/> <img alt="Cart screenshot" src="screenshots/s04_cart.png" height="400"/> <img alt="Checkout screenshot" src="screenshots/s05_checkout.png" height="400"/>


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
