# TFG — Automated Skin Lesion Analysis with Deep Learning and LLM Integration

**Author:** Natalia García Sánchez  

**Supervisor:** Rubén Juárez Cádiz  

**Institution:** CEU San Pablo University, Madrid  

**Degree:** Biomedical Engineering  

**Year:** 2026

---

## Overview

This repository contains the full source code for a TFG (Bachelor's Thesis) on automated dermoscopic skin lesion analysis. The system combines three components:

1. **Lesion segmentation** — U-Net++ with EfficientNet-B5 encoder trained on ISIC 2018

2. **Eight-class classification** — EfficientNet-B4 trained on ISIC 2019, with a controlled comparison between classification on original images (Model A) and segmentation-masked images (Model B)

3. **LLM-based explanation generation** — conversational diagnostic prototype connecting the trained models to Claude via the Model Context Protocol (MCP)

---

## Repository Structure
