# Tenant Management System

A Kotlin Android app for landlords to record tenants, track rent payments, and contact tenants.

## Download

Once v1.0 is published, download the signed `app-release.apk` from the Assets section on [GitHub Releases](https://github.com/Zacharia223/Mobile-application-development/releases).

## Features

- Register and sign in.
- Add tenants and see the tenant count and total rent on the dashboard.
- View saved tenants alphabetically.
- Open the phone dialer from a tenant record.
- Record rent payments and receive payment reminders.

## Architecture

The tenant screens in `ui/` share a `TenantViewModel`. They observe its LiveData to refresh the tenant list, count, and total rent. In `data/`, `TenantRepository` uses `TenantDao` to save and retrieve tenant records in Room.

## Run locally

Clone this repository and open it in Android Studio. Let Gradle sync, select the `app` configuration, choose a device or emulator, and click **Run**.

## Team contributions

| Stage | Member | Evidence |
| --- | --- | --- |
| 1. Repository and packages | Zacharia Ogega | [Setup commit](../../commit/b6f5d2b) |
| 2. Tenant ViewModel | Myles Mark Otieno | [PR #1](../../pull/1) |
| 3. Room database | Fidel Thomas Onunga | [PR #3](../../pull/3) |
| 4. Screen integration | Ngumbau Christine Ngina | [PR #2](../../pull/2) |
| 5. Tenant persistence | Mark William Ochieng | [PR #4](../../pull/4) |
| 6. README and signed release | Zacharia Ogega | [Release PR](../../pulls?q=is%3Apr+head%3Arelease%2Fv1.0) |
